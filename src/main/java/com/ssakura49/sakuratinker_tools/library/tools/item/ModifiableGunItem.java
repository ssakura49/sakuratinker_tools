package com.ssakura49.sakuratinker_tools.library.tools.item;

import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import com.ssakura49.sakuratinker_tools.client.screen.menu.RevolverMenu;
import com.ssakura49.sakuratinker_tools.content.bullet.BulletManager;
import com.ssakura49.sakuratinker_tools.content.bullet.BulletRegistry;
import com.ssakura49.sakuratinker_tools.content.capability.ToolBulletSlotCapability;
import com.ssakura49.sakuratinker_tools.content.item.BulletItem;
import com.ssakura49.sakuratinker_tools.library.interfaces.item.slot.IHasBulletInventory;
import com.ssakura49.sakuratinker_tools.library.tools.hook.bullet.BulletAmmoModifierHook;
import com.ssakura49.sakuratinker_tools.network.PacketHandler;
import com.ssakura49.sakuratinker_tools.network.s2c.RecoilPacket;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.UsingToolModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.function.Consumer;
import java.util.function.Predicate;


public abstract class ModifiableGunItem extends ModifiableItem implements IHasBulletInventory {

   public ModifiableGunItem(Properties properties, ToolDefinition toolDefinition) {
      super(properties, toolDefinition);
   }

   @Override
   public int getDefaultSlots() {
      return setBulletSlots();
   }

   public abstract int setBulletSlots();

   public abstract int getDefaultProjectileRange();

   public abstract int chargeTime();

   public abstract int intervalTick();

   protected abstract SoundEvent getShootSound();

   protected float getProjectileGravity() {
      return 0.0F;
   }

   protected float getFalloffStart() {
      return 35.0F;
   }

   protected float getFalloffEnd() {
      return 100.0F;
   }

   protected float getPenetrationDrop() {
      return 0.15F;
   }

   protected String getBulletType() {
      return BulletRegistry.NORMAL;
   }

   protected SoundEvent getEmptySound() {
      return SoundEvents.DISPENSER_FAIL;
   }

   protected void onRoundConsumed(ItemStack stack, Level level, Player player, ToolStack gun, ToolBulletSlotCapability capability) {
   }

   protected void onEmptyAmmo(ItemStack stack, Level level, Player player) {
      player.stopUsingItem();
      if (!level.isClientSide) {
         player.playSound(this.getEmptySound(), 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
      }
   }

   protected boolean canFire(ItemStack stack, Level level, Player player) {
      return true;
   }


   public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
      return ToolBulletSlotCapability::isBulletAmmo;
   }

   @Override
   public @NotNull UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.NONE;
   }

   @Override
   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   @Override
   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      consumer.accept(new IClientItemExtensions() {
         public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            return !itemStack.isEmpty() ? HumanoidModel.ArmPose.CROSSBOW_HOLD : HumanoidModel.ArmPose.EMPTY;
         }
      });
   }

   public void tryReload(ItemStack stack, Level level, Player player) {
      ToolStack gun = ToolStack.from(stack);
      ToolBulletSlotCapability capability = ToolBulletSlotCapability.get(stack);
      if (capability == null) {
         return;
      }
      this.setSlots(capability);
      int current = BulletAmmoModifierHook.getCurrentChamber(gun, capability);
      int next = BulletAmmoModifierHook.findNextUsableSlot(capability, current + 1, ToolBulletSlotCapability::isBulletAmmo);
      if (next >= 0) {
         BulletAmmoModifierHook.setCurrentChamber(gun, capability, next);
      }
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (player.isShiftKeyDown()) {
         InteractionResultHolder<ItemStack> menu = openAmmoMenu(stack, level, player, hand);
         if (menu != null) {
            return menu;
         }
      }
      if (!this.canFire(stack, level, player)) {
         return InteractionResultHolder.fail(stack);
      }

      ToolBulletSlotCapability capability = ToolBulletSlotCapability.get(stack);
      if (capability != null) {
         this.setSlots(capability);
         if (capability.hasAmmo(ToolBulletSlotCapability::isBulletAmmo)) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
         }
      }
      if (!level.isClientSide) {
         player.playSound(this.getEmptySound(), 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
      }
      return InteractionResultHolder.fail(stack);
   }

   protected InteractionResultHolder<ItemStack> openAmmoMenu(ItemStack stack, Level level, Player player, InteractionHand hand) {
      if (!level.isClientSide) {
         player.openMenu(new SimpleMenuProvider((id, inv, p) -> new RevolverMenu(id, inv, stack), stack.getHoverName()));
      }
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
   }

   @Override
   public void onUseTick(Level level, LivingEntity living, ItemStack stack, int timeLeft) {
      if (level.isClientSide) {
         return;
      }
      if (!(living instanceof Player player)) {
         return;
      }

      ToolStack gun = ToolStack.from(stack);
      if (gun.isBroken()) {
         player.stopUsingItem();
         return;
      }
      ToolBulletSlotCapability capability = ToolBulletSlotCapability.get(stack);
      if (capability == null) {
         player.stopUsingItem();
         return;
      }
      this.setSlots(capability);
      if (!this.canFire(stack, level, player)) {
         player.stopUsingItem();
         return;
      }
      int elapsed = getUseDuration(stack) - timeLeft;
      if (elapsed < chargeTime()) {
         return;
      }
      if (elapsed % intervalTick() != 0) {
         return;
      }
      ItemStack round = BulletAmmoModifierHook.getAmmo(gun, capability, player, ToolBulletSlotCapability::isBulletAmmo);
      if (round.isEmpty()) {
         this.onEmptyAmmo(stack, level, player);
         return;
      }

      fireBullet(level, player, round, gun);
      BulletAmmoModifierHook.consumeAmmo(gun, capability, player, ToolBulletSlotCapability::isBulletAmmo);
      this.onRoundConsumed(stack, level, player, gun, capability);
      for (ModifierEntry entry : gun.getModifierList()) {
         ((UsingToolModifierHook) entry.getHook(ModifierHooks.TOOL_USING))
                 .onUsingTick(gun, entry, player, getUseDuration(stack), timeLeft, ModifierEntry.EMPTY);
      }
   }

   protected void fireBullet(Level level, LivingEntity shooter, ItemStack bulletStack, ToolStack gunTool) {
      if (level.isClientSide) {
         return;
      }
      ToolStack ammoTool = bulletStack.getItem() instanceof ModifiableBulletItem ? ToolStack.from(bulletStack) : null;
      String bulletType = ammoTool != null
              ? getBulletType()
              : (bulletStack.getItem() instanceof BulletItem bullet ? bullet.getBulletType() : getBulletType());
      float damage = ammoTool != null
              ? ammoTool.getStats().get(ToolStats.ATTACK_DAMAGE)
              : (bulletStack.getItem() instanceof BulletItem bullet ? bullet.getBulletDamage() : gunTool.getStats().get(ToolStats.ATTACK_DAMAGE));

      boolean crit = isVanillaCrit(shooter);
      Vec3 direction = applySpread(level.random, shooter.getLookAngle(), gunTool.getStats().get(STToolStats.SPREAD));
      float velocity = gunTool.getStats().get(ToolStats.VELOCITY) + 3.0F;
      Vec3 start = shooter.position().add(0, shooter.getEyeHeight() - 0.1, 0);

      BulletManager.spawnBullet(
              bulletType, level, shooter, ammoTool, start, direction,
              damage, getPenetrationDrop(), crit, velocity,
              getProjectileGravity(), getFalloffStart(), getFalloffEnd()
      );

      level.playSound(null, shooter.blockPosition(), getShootSound(), SoundSource.PLAYERS, 1.0F, 1.0F);

      if (shooter instanceof ServerPlayer serverPlayer) {
         float recoil = gunTool.getStats().get(STToolStats.RECOIL);
         PacketHandler.sendToPlayer(new RecoilPacket(recoil), serverPlayer);
      }
   }

   private static boolean isVanillaCrit(LivingEntity shooter) {
      return shooter instanceof Player player
              && player.fallDistance > 0.0F
              && !player.onGround()
              && !player.onClimbable()
              && !player.isInWater()
              && !player.hasEffect(MobEffects.BLINDNESS)
              && !player.isPassenger()
              && !player.isSprinting();
   }

   private static Vec3 applySpread(RandomSource random, Vec3 direction, float halfAngleDeg) {
      Vec3 forward = direction.normalize();
      double halfRad = Math.toRadians(halfAngleDeg);
      double tanHalf = Math.tan(halfRad);
      Vec3 helper = Math.abs(forward.x) < 0.99 ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0);
      Vec3 right = forward.cross(helper).normalize();
      Vec3 up = forward.cross(right).normalize();
      double radius = tanHalf * Math.sqrt(random.nextDouble());
      double angle = random.nextDouble() * Math.PI * 2.0;
      double offsetX = Math.cos(angle) * radius;
      double offsetY = Math.sin(angle) * radius;
      Vec3 offset = right.scale(offsetX).add(up.scale(offsetY));
      return forward.add(offset).normalize();
   }
}
