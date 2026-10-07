package com.ssakura49.sakuratinker_tools.content.tool.item;

import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import com.ssakura49.sakuratinker_tools.content.capability.ToolBulletSlotCapability;
import com.ssakura49.sakuratinker_tools.init.STTSound;
import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableGunItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class RevolverItem extends ModifiableGunItem {
   public static final int MAX_BULLETS = 6;

   public RevolverItem(Item.Properties props, ToolDefinition def) {
      super(props, def);
   }

   @Override
   public int setBulletSlots() {
      return MAX_BULLETS;
   }

   @Override
   public int getDefaultProjectileRange() {
      return 200;
   }

   @Override
   public int chargeTime() {
      return 4;
   }

   @Override
   public int intervalTick() {
      return 3;
   }

   public int getReloadTime() {
      return 60;
   }

   @Override
   protected SoundEvent getShootSound() {
      return STTSound.REVOLVER_SHOOT.get();
   }

  public static final ResourceLocation KEY_RELOAD_START = com.ssakura49.sakuratinker.SakuraTinker.getResource("bullet_reload_start");

  public static final ResourceLocation KEY_ROUND_SHOTS = com.ssakura49.sakuratinker.SakuraTinker.getResource("round_shots");

   public boolean isReloading(ItemStack stack, Level level) {
      if (level == null) return false;
      ToolStack tool = ToolStack.from(stack);
      if (!tool.getPersistentData().contains(KEY_RELOAD_START)) return false;
      long start = tool.getPersistentData().getInt(KEY_RELOAD_START);
      return level.getGameTime() - start < getReloadTime();
   }

   private void startReload(ItemStack stack, Level level) {
      ToolStack.from(stack).getPersistentData().putInt(KEY_RELOAD_START, (int) level.getGameTime());
   }

   public float getReloadSpinAngle(ItemStack stack, Level level) {
      if (level == null) return 0.0F;
      ToolStack tool = ToolStack.from(stack);
      if (!tool.getPersistentData().contains(KEY_RELOAD_START)) return 0.0F;
      long start = tool.getPersistentData().getInt(KEY_RELOAD_START);
      long elapsed = level.getGameTime() - start;
      int reloadTime = getReloadTime();
      if (elapsed < 0 || elapsed >= reloadTime) return 0.0F;
      return (float) elapsed / (float) reloadTime * 720.0F;
   }

   @Override
   protected SoundEvent getEmptySound() {
      return SoundEvents.LEVER_CLICK;
   }

   private static int getRoundShots(ItemStack stack) {
      return ToolStack.from(stack).getPersistentData().getInt(KEY_ROUND_SHOTS);
   }

   private static void setRoundShots(ItemStack stack, int count) {
      ToolStack.from(stack).getPersistentData().putInt(KEY_ROUND_SHOTS, Math.max(0, count));
   }

   @Override
   protected void onRoundConsumed(ItemStack stack, Level level, Player player, ToolStack gun, ToolBulletSlotCapability capability) {
      int roundShots = getRoundShots(stack) + 1;
      if (roundShots >= capability.getSlots()) {
         this.startReload(stack, level);
         player.stopUsingItem();
         setRoundShots(stack, 0);
         //System.out.println("[STT-RELOAD] round complete (" + capability.getSlots() + " shots) -> reload started, gameTime=" + level.getGameTime());
      } else {
         setRoundShots(stack, roundShots);
      }
   }

   @Override
   protected void onEmptyAmmo(ItemStack stack, Level level, Player player) {
      super.onEmptyAmmo(stack, level, player);
      setRoundShots(stack, 0);
   }

   @Override
   protected boolean canFire(ItemStack stack, Level level, Player player) {
      return !isReloading(stack, level);
   }

   @Override
   public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
      super.releaseUsing(stack, level, entity, timeLeft);
      setRoundShots(stack, 0);
   }

   @Override
   public List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
      TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
      builder.addDurability();
      builder.add(ToolStats.ATTACK_DAMAGE);
      builder.add(ToolStats.ATTACK_SPEED);
      builder.add(ToolStats.VELOCITY);
      builder.add(ToolStats.ACCURACY);
      builder.add(STToolStats.RECOIL);
      builder.add(STToolStats.SPREAD);
      builder.addAllFreeSlots();
      for (ModifierEntry entry : tool.getModifierList()) {
         entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, flag);
      }
      return tooltips;
   }
}
