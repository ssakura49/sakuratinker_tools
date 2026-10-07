package com.ssakura49.sakuratinker_tools.content.entity;

import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.core.utils.ProjectileUtils;
import com.ssakura49.sakuratinker_tools.init.STTEntities;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ScheduledProjectileTaskModifierHook;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Schedule;
import slimeknights.tconstruct.tools.entity.ToolProjectile;

public class TinkerArrowEntity extends AbstractArrow implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage, ToolProjectile {
   private ItemStack arrowStack;
   private ToolStack toolStack;
   private StatsNBT stats;
   private float bonusDamage = 0.0F;
   private boolean critical = false;
   private final Set<Integer> piercedEntityIds = new IntOpenHashSet();
   private final List<Entity> killedEntities = new ArrayList<>();
   private boolean dealtDamage = false;
   private Schedule tasks = Schedule.EMPTY;
   protected static final EntityDataAccessor<Float> WATER_INERTIA = SynchedEntityData.defineId(TinkerArrowEntity.class, EntityDataSerializers.FLOAT);

   public TinkerArrowEntity(EntityType<? extends TinkerArrowEntity> type, Level level) {
      super(type, level);
      this.arrowStack = ItemStack.EMPTY;
      this.toolStack = null;
      this.stats = StatsNBT.EMPTY;
   }
   public TinkerArrowEntity(Level level, double x, double y, double z) {
      super(STTEntities.TINKER_ARROW_ENTITY.get(), level);
   }
   public TinkerArrowEntity(Level level, LivingEntity entity) {
      super(STTEntities.TINKER_ARROW_ENTITY.get(), entity, level);
      this.setOwner(entity);
      this.arrowStack = ItemStack.EMPTY;
      this.toolStack = null;
      this.stats = StatsNBT.EMPTY;
      this.pickup = Pickup.DISALLOWED;
   }

   public TinkerArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
      super(STTEntities.TINKER_ARROW_ENTITY.get(),  level);
   }

   public IToolStackView onCreate(ItemStack stack, @Nullable LivingEntity shooter) {
      setTool(stack.copyWithCount(1));
      IToolStackView tool = getTool();
      if (tool != null) {
         EntityModifierCapability.getCapability(this).addModifiers(tool.getModifiers());
         setBaseDamage(ConditionalStatModifierHook.getModifiedStat(tool, shooter, ToolStats.ATTACK_DAMAGE));
         this.entityData.set(WATER_INERTIA, ConditionalStatModifierHook.getModifiedStat(tool, shooter, ToolStats.WATER_INERTIA));
      }
      return tool;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(WATER_INERTIA, 0.6F);
   }

   @Override
   protected float getWaterInertia() {
      return this.entityData.get(WATER_INERTIA);
   }

   public void setTool(ItemStack tool) {
      if (!tool.isEmpty()) {
         this.arrowStack = tool.copy();
         this.toolStack = ToolStack.from(tool);
         this.stats = this.toolStack.getStats();
      } else {
         this.arrowStack = ItemStack.EMPTY;
         this.toolStack = null;
         this.stats = StatsNBT.EMPTY;
      }
   }

   @Override
   public void setKnockback(int knockback) {
      super.setKnockback(getKnockback() + knockback);
   }

   @Override
   public void setPierceLevel(byte pierceLevel) {
      super.setPierceLevel((byte) (getPierceLevel() + pierceLevel));
   }

   @Override
   public void setCritical(boolean critical) {
      this.critical = critical;
   }
   @Override
   public boolean getCritical() {
      return this.critical;
   }
   @Override
   public void setDamage(float damage) {
      this.bonusDamage = damage;
   }
   @Override
   public float getDamage() {
      return calculateModifiedDamage();
   }
   private float calculateModifiedDamage() {
      float baseDamage = this.stats.get(ToolStats.ATTACK_DAMAGE) + bonusDamage;
      float speed = (float)this.getDeltaMovement().length();
      float damage = Mth.clamp(speed * baseDamage, 0.0F, Integer.MAX_VALUE);
      if (this.getCritical()) {
         damage += this.random.nextInt((int)(damage / 2.0F + 2));
      }
      return damage;
   }
   public ToolStack getTool() {
      return this.toolStack;
   }

   @Override
   public @NotNull Component getDisplayName() {
      return this.arrowStack.getDisplayName();
   }

   private boolean shouldIgnoreEntity(Entity entity) {
      return piercedEntityIds.contains(entity.getId());
   }

   private void markEntityPierced(Entity entity) {
      piercedEntityIds.add(entity.getId());
   }

   private void recordKilledEntity(Entity entity) {
      if (!entity.isAlive()) {
         killedEntities.add(entity);
      }
   }
   private void clearPiercingData() {
      piercedEntityIds.clear();
      killedEntities.clear();
   }
   private boolean isPiercingLimitReached() {
      return getPierceLevel() > 0 && piercedEntityIds.size() >= getPierceLevel() + 1;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.put("Tool", arrowStack.serializeNBT());
      tag.putFloat("BonusDamage", bonusDamage);
      tag.putBoolean("Critical", critical);
      tag.putBoolean("DealtDamage", dealtDamage);
   }
   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.setTool(ItemStack.of(tag.getCompound("Tool")));
      this.bonusDamage = tag.getFloat("BonusDamage");
      this.critical = tag.getBoolean("Critical");
      this.dealtDamage = tag.getBoolean("DealtDamage");
   }

   @Override
   public boolean tryPickup(@NotNull Player player) {
      return false;
   }

   @Override
   public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
      if (!this.arrowStack.isEmpty()) {
         IToolStackView tool = getTool();
         LivingEntity shooter = getOwner() instanceof LivingEntity living ? living : null;
         int vlevel = Mth.floor(ConditionalStatModifierHook.getModifiedStat(tool, shooter, ToolStats.VELOCITY));
         int alevel = Mth.floor(ModifierUtil.getInaccuracy(tool, shooter));
         velocity *= (1.0F + vlevel * 0.2F);
         inaccuracy *= (0.75F + alevel * 0.1F);
         super.shoot(x, y, z, velocity, inaccuracy);
         ModDataNBT arrowData = PersistentDataCapability.getOrWarn(this);
         for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.PROJECTILE_SHOT).onProjectileShoot(tool, entry, shooter, arrowStack, this, this, arrowData, true);
         }
         this.tasks = ScheduledProjectileTaskModifierHook.createSchedule(tool, arrowStack, this, this, arrowData);
      } else {
         super.shoot(x, y, z, velocity, inaccuracy);
      }

   }

   @Override
   public void tick() {
      super.tick();
      if (!tasks.isEmpty() && !arrowStack.isEmpty()) {
         ScheduledProjectileTaskModifierHook.checkSchedule(getTool(), arrowStack, this, this, tasks);
      }
   }

   @Override
   protected void onHitEntity(EntityHitResult result) {
      Entity target = result.getEntity();
      if (isPiercingLimitReached()) {
         discard();
         return;
      }

      markEntityPierced(target);
      if (target instanceof LivingEntity livingTarget) {
         handleLivingTargetHit(livingTarget, result);
      } else {
         handleNonLivingTargetHit(result);
      }
   }

   private void handleLivingTargetHit(LivingEntity target, EntityHitResult result) {
      float prevHealth = target.getHealth();
      boolean attackSuccess = attemptAttack(target);

      if (attackSuccess) {
         recordKilledEntity(target);
         triggerAchievementsIfNeeded();
         playHitSound();
      } else {
         revertTargetHealth(target, prevHealth);
         bounceArrow();
      }

      tryDiscardIfNoPierce();
   }

   private boolean attemptAttack(LivingEntity target) {
      return ProjectileUtils.arrowAttackEntity(
              arrowStack.getItem(),
              this,
              toolStack,
              (LivingEntity) getOwner(),
              target,
              this.toolStack.getStats().get(ToolStats.ACCURACY),
              (float) (1.0F + 0.1 * this.toolStack.getStats().get(ToolStats.VELOCITY)),
              false
      );
   }

   private void triggerAchievementsIfNeeded() {
      if (level().isClientSide || !isCritArrow()) return;

      Entity shooter = getOwner();
      if (shooter instanceof ServerPlayer serverPlayer) {
         if (!killedEntities.isEmpty()) {
            CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, killedEntities);
         }
      }
   }

   private void revertTargetHealth(LivingEntity target, float prevHealth) {
      target.setHealth(prevHealth);
   }

   private void bounceArrow() {
      this.dealtDamage = true;
      setDeltaMovement(getDeltaMovement().scale(-0.1));
      setYRot(getYRot() + 180.0F);
      yRotO += 180.0F;
   }

   @Override
   @Nullable
   protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
      return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
   }

   private void tryDiscardIfNoPierce() {
      if (getPierceLevel() <= 0) {
         discard();
      }
   }

   private void handleNonLivingTargetHit(EntityHitResult result) {
      Entity target = result.getEntity();
      DamageSource source = this.level().damageSources().arrow(this, this.getOwner());
      target.hurt(source, getDamage());
      playHitSound();
      discard();
   }

   private void playHitSound() {
      if (!this.isSilent()) {
         this.level().playSound(
                 (Player) this.getOwner(),
                 this.getX(), this.getY(), this.getZ(),
                 SoundEvents.ARROW_HIT,
                 this.getSoundSource(),
                 1.0F,
                 1.2F / (this.random.nextFloat() * 0.2F + 0.8F)
         );
      }
   }

   @Override
   protected boolean canHitEntity(Entity entity) {
      if (entity instanceof EndCrystal) {
         return true;
      }
      return super.canHitEntity(entity) && !shouldIgnoreEntity(entity);
   }

   @Override
   public @NotNull ItemStack getPickupItem() {
      return this.arrowStack;
   }

   @Override
   public void writeSpawnData(FriendlyByteBuf buffer) {
      buffer.writeItem(this.arrowStack);
      buffer.writeFloat(this.bonusDamage);
      buffer.writeBoolean(this.critical);
   }
   @Override
   public void readSpawnData(FriendlyByteBuf buffer) {
      this.setTool(buffer.readItem());
      this.bonusDamage = buffer.readFloat();
      this.critical = buffer.readBoolean();
   }

   @Override
   public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   public ItemStack getDisplayTool() {
      return arrowStack;
   }
}
