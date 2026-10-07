package com.ssakura49.sakuratinker_tools.library.tools.hook.bullet;

import com.ssakura49.sakuratinker_tools.content.capability.ToolBulletSlotCapability;
import com.ssakura49.sakuratinker_tools.library.tools.hook.STTHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * 枪械弹药 hook（STTHooks.BULLET_AMMO）： * 修饰符可以实现 {@link #findAmmo} 提供自定义弹药来源（如自动装填、特殊弹种），并参与扣弹。 * 静态方法是枪械的统一弹药入口： * <ul> *   <li>{@link #hasAmmo} — 是否有可发射的弹药</li> *   <li>{@link #getAmmo} — 读取下一发（膛位优先，空/损坏则循环扫描，最后询问修饰符），无副作用</li> *   <li>{@link #consumeAmmo} — 发射并扣除一发（写回弹仓 + 推进膛位），返回被消耗的子弹副本</li> * </ul> */
public interface BulletAmmoModifierHook {

   BulletAmmoModifierHook EMPTY = (tool, modifier, shooter, capability, predicate) -> ItemStack.EMPTY;

   /** 从弹仓中寻找可用子弹（修饰符自定义来源） */
   ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter,
                      ToolBulletSlotCapability capability, Predicate<ItemStack> predicate);

   /**
    * 默认消耗子弹
    * @param tool 工具
    * @param modifier Modifier entry
    * @param shooter 发射者
    * @param ammo 子弹 ItemStack
    * @param needed 消耗数量
    */
   default void shrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, int needed) {
      if (ammo.getItem() instanceof IModifiable) {
         ToolStack ammoStack = ToolStack.from(ammo);
         ToolDamageUtil.damageAnimated(ammoStack, needed, shooter);
      } else {
         ammo.shrink(needed);
      }
   }

   /** 通用弹药判定（与 ToolBulletSlotCapability.isBulletAmmo 一致） */
   static boolean isUsableAmmo(ItemStack stack) {
      return ToolBulletSlotCapability.isBulletAmmo(stack);
   }

   /** 是否有弹药 */
   static boolean hasAmmo(IToolStackView tool, ItemStack gun, LivingEntity shooter, Predicate<ItemStack> predicate) {
      ToolBulletSlotCapability capability = ToolBulletSlotCapability.get(gun);
      return capability != null && hasAmmo(tool, capability, shooter, predicate);
   }

   /** 是否有弹药 */
   static boolean hasAmmo(IToolStackView tool, ToolBulletSlotCapability capability, LivingEntity shooter, Predicate<ItemStack> predicate) {
      return !getAmmo(tool, capability, shooter, predicate).isEmpty();
   }

   /** 获取当前格的弹药（读取，无副作用） */
   static ItemStack getAmmo(IToolStackView tool, ItemStack gun, LivingEntity shooter, Predicate<ItemStack> predicate) {
      ToolBulletSlotCapability capability = ToolBulletSlotCapability.get(gun);
      return capability == null ? ItemStack.EMPTY : getAmmo(tool, capability, shooter, predicate);
   }

   /**
    * 读取下一发（无副作用）：
    * 1) 当前膛位可用 -> 直接返回；    * 2) 当前膛位空/损坏 -> 向前循环扫描到下一颗可用子弹；    * 3) 弹仓全空 -> 询问修饰符的自定义弹药来源。    */
   static ItemStack getAmmo(IToolStackView tool, ToolBulletSlotCapability capability, LivingEntity shooter, Predicate<ItemStack> predicate) {
      int current = getCurrentChamber(tool, capability);
      ItemStack bullet = capability.getStackInSlot(current);
      if (!bullet.isEmpty() && predicate.test(bullet)) return bullet;

      int found = findNextUsableSlot(capability, current, predicate);
      if (found >= 0) return capability.getStackInSlot(found);

      for (ModifierEntry entry : tool.getModifierList()) {
         BulletAmmoModifierHook hook = entry.getHook(STTHooks.BULLET_AMMO);
         if (hook == EMPTY) continue;
         ItemStack ammo = hook.findAmmo(tool, entry, shooter, capability, predicate);
         if (!ammo.isEmpty()) return ammo;
      }
      return ItemStack.EMPTY;
   }

   /** 发射并消耗一发（写回弹仓 + 推进膛位），返回被消耗的子弹副本；无弹返回 EMPTY */
   static ItemStack consumeAmmo(IToolStackView tool, ItemStack gun, LivingEntity shooter, Predicate<ItemStack> predicate) {
      ToolBulletSlotCapability capability = ToolBulletSlotCapability.get(gun);
      return capability == null ? ItemStack.EMPTY : consumeAmmo(tool, capability, shooter, predicate);
   }

   /**
    * 发射并消耗一发（写回弹仓 + 推进膛位）：    * 1) 从当前膛位起循环扫描到一颗可用子弹，扣除（可打造子弹扣耐久，普通子弹减数量）并写回，推进膛位后返回副本；    * 2) 弹仓全空 -> 询问修饰符自定义弹药，消耗后推进膛位返回副本；    * 3) 都没有 -> EMPTY。    */
   static ItemStack consumeAmmo(IToolStackView tool, ToolBulletSlotCapability capability, LivingEntity shooter, Predicate<ItemStack> predicate) {
      int slotCount = Math.max(1, capability.getSlots());
      int current = getCurrentChamber(tool, capability);

      // 1) 弹仓扫描：找到下一颗可用子弹
      int slot = findNextUsableSlot(capability, current, predicate);
      if (slot >= 0) {
         ItemStack ammo = capability.getStackInSlot(slot);
         shrinkNormal(tool, capability, slot, ammo, shooter);
         // 膛位推进到下一颗可用子弹（跳过空格）；全部为空则回绕 +1，下次 getAmmo 会重新扫描
         int nextChamber = findNextUsableSlot(capability, slot + 1, predicate);
         setCurrentChamber(tool, capability, nextChamber >= 0 ? nextChamber : (slot + 1) % slotCount);
         return ItemHandlerHelper.copyStackWithSize(ammo, 1);
      }

      // 2) 修饰符自定义弹药来源（自动装填等）
      for (ModifierEntry entry : tool.getModifierList()) {
         BulletAmmoModifierHook hook = entry.getHook(STTHooks.BULLET_AMMO);
         if (hook == EMPTY) continue;
         ItemStack customAmmo = hook.findAmmo(tool, entry, shooter, capability, predicate);
         if (!customAmmo.isEmpty()) {
            hook.shrinkAmmo(tool, entry, shooter, customAmmo, 1);
            setCurrentChamber(tool, capability, (current + 1) % slotCount);
            return ItemHandlerHelper.copyStackWithSize(customAmmo, 1);
         }
      }

      return ItemStack.EMPTY;
   }

   /** 普通子弹的扣弹：可打造子弹扣 1 耐久，普通子弹减 1 数量，并写回弹仓（必须写回，否则永不消耗） */
   private static void shrinkNormal(IToolStackView tool, ToolBulletSlotCapability capability, int slot, ItemStack ammo, LivingEntity shooter) {
      if (ammo.getItem() instanceof IModifiable) {
         ToolStack ammoStack = ToolStack.from(ammo);
         ToolDamageUtil.damageAnimated(ammoStack, 1, shooter);
      } else {
         ammo.shrink(1);
      }
      capability.setStackInSlot(slot, ammo.isEmpty() ? ItemStack.EMPTY : ammo);
   }

   /** 从 startSlot 起循环扫描下一颗可用子弹的槽位；找不到返回 -1 */
   static int findNextUsableSlot(ToolBulletSlotCapability capability, int startSlot, Predicate<ItemStack> predicate) {
      int slotCount = Math.max(1, capability.getSlots());
      for (int i = 0; i < slotCount; i++) {
         int idx = (startSlot + i) % slotCount;
         ItemStack slot = capability.getStackInSlot(idx);
         if (!slot.isEmpty() && predicate.test(slot)) return idx;
      }
      return -1;
   }

   /** 获取当前膛位索引（默认 0） */
   static int getCurrentChamber(IToolStackView tool, ToolBulletSlotCapability cap) {
      return tool.getPersistentData().getInt(ToolBulletSlotCapability.KEY_CURRENT_CHAMBER);
   }

   /** 设置当前膛位索引（取模防越界） */
   static void setCurrentChamber(IToolStackView tool, ToolBulletSlotCapability cap, int slot) {
      ModDataNBT data = tool.getPersistentData();
      int slotCount = Math.max(1, cap.getSlots());
      data.putInt(ToolBulletSlotCapability.KEY_CURRENT_CHAMBER, Math.floorMod(slot, slotCount));
   }

   /** 推进膛位（开火或空仓时调用） */
   static void advanceChamber(IToolStackView tool, ToolBulletSlotCapability cap) {
      setCurrentChamber(tool, cap, getCurrentChamber(tool, cap) + 1);
   }
}
