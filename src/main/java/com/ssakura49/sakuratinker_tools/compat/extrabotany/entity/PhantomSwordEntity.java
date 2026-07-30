package com.ssakura49.sakuratinker_tools.compat.extrabotany.entity;

import com.mojang.logging.LogUtils;
import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.core.utils.ProjectileUtils;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBEntity;
import com.ssakura49.sakuratinker_tools.content.entity.CelestialBladeEntity;
import com.ssakura49.sakuratinker_tools.content.entity.part.CelestialBladePart;
import com.ssakura49.sakuratinker_tools.init.STTEntities;
import javax.annotation.Nonnull;

import com.ssakura49.sakuratinker_tools.init.STTEntityDataSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.UUID;

public class PhantomSwordEntity extends ThrowableProjectile implements  IEntityAdditionalSpawnData,IProjectileCritical, IProjectileDamage, TraceableEntity {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final int LIVE_TICKS = 30;
   private static final String TAG_VARIETY = "variety";
   private static final String TAG_ROTATION = "rotation";
   private static final String TAG_PITCH = "pitch";
   private static final String TAG_TARGETPOS = "targetpos";
   private static final String TAG_DELAY = "delay";
   private static final String TAG_FAKE = "fake";
   private static final String TAG_TOOL = "tool";
   private static final String TAG_OWNER = "owner";
   private static final EntityDataAccessor<Integer> VARIETY = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<BlockPos> TARGET_POS = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.BLOCK_POS);
   private static final EntityDataAccessor<Boolean> FAKE = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.ITEM_STACK);
   public ItemStack getItemStack() {
      return getEntityData().get(DATA_ITEM_STACK);
   }

   public void setItemStack(ItemStack stack) {
      entityData.set(DATA_ITEM_STACK,stack);
   }

   protected static final EntityDataAccessor<CelestialBladePart> ZENITH_PART = SynchedEntityData.defineId(PhantomSwordEntity.class, STTEntityDataSerializers.CELESTIAL_BLADE_PART.get());
   public void setZenithPart(CelestialBladePart part) {
      this.getEntityData().set(ZENITH_PART, part);
   }
   public CelestialBladePart getZenithPart() {
      return this.getEntityData().get(ZENITH_PART);
   }

   private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(
           PhantomSwordEntity.class,
           EntityDataSerializers.FLOAT
   );
   public void setSpeed(float speed) {
      entityData.set(SPEED,speed);
   }
   public float getSpeed() {
      return entityData.get(SPEED);
   }

   protected static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(PhantomSwordEntity.class, EntityDataSerializers.INT);

   public void setOwnerId(int ownerId) {
      this.getEntityData().set(OWNER_ID, ownerId);
   }

   public int getOwnerId() {
      return this.getEntityData().get(OWNER_ID);
   }

   private static final float[][] rgb = { { 0.82F, 0.2F, 0.58F }, { 0F, 0.71F, 0.10F }, { 0.74F, 0.07F, 0.32F },
           { 0.01F, 0.45F, 0.8F }, { 0.05F, 0.39F, 0.9F }, { 0.38F, 0.34F, 0.42F }, { 0.41F, 0.31F, 0.14F },
           { 0.92F, 0.92F, 0.21F }, { 0.61F, 0.92F, 0.98F }, { 0.18F, 0.45F, 0.43F } };

   private Entity thrower;
   private UUID ownerId;
   private ItemStack itemStack;
   private ToolStack toolStack;
   private StatsNBT stats;
   private boolean critical = false;
   private float bonusDamage = 0.0F;

   public PhantomSwordEntity(EntityType<PhantomSwordEntity> type, Level worldIn) {
      super(type, worldIn);
      this.itemStack = ItemStack.EMPTY;
      this.toolStack = null;
      this.stats = StatsNBT.EMPTY;
   }

   public PhantomSwordEntity(Level worldIn) {
      super(EBEntity.PHANTOM_SWORD.get(), worldIn);
      this.itemStack = ItemStack.EMPTY;
      this.toolStack = null;
      this.stats = StatsNBT.EMPTY;
   }

   public PhantomSwordEntity(Level world, LivingEntity thrower, BlockPos targetpos, ItemStack tool) {
      super(EBEntity.PHANTOM_SWORD.get(), world);
      this.thrower = thrower;
      this.setOwner(thrower);
      this.setTool(tool);
      setTargetPos(targetpos);
      setVariety((int) (10 * Math.random()));
   }

   public PhantomSwordEntity(Level level, LivingEntity thrower, BlockPos targetpos) {
      super(EBEntity.PHANTOM_SWORD.get(), level);
      this.thrower = thrower;
      this.setOwner(thrower);
      setTargetPos(targetpos);
      setVariety((int) (10 * Math.random()));
   }

   public PhantomSwordEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
      super(EBEntity.PHANTOM_SWORD.get(),level);
   }

   public void setTool(ItemStack tool) {
      if (!tool.isEmpty()) {
         this.itemStack = tool.copy();
         this.toolStack = ToolStack.from(tool);
         this.stats = this.toolStack.getStats();
      } else {
         this.itemStack = ItemStack.EMPTY;
         this.toolStack = null;
         this.stats = StatsNBT.EMPTY;
      }
   }


   @Override
   @Nullable
   public Entity getOwner() {
      return thrower;
   }

   public void setOwner(Entity owner) {
      this.ownerId = owner.getUUID();
      this.thrower = owner;
      if (!level().isClientSide) {
         setOwnerId(owner.getId());
      }
   }


   public ToolStack getTool() {
      return this.toolStack;
   }
   public void setCritical(boolean critical) {
      this.critical = critical;
   }
   public boolean getCritical() {
      return this.critical;
   }
   public void setDamage(float damage) {
      this.bonusDamage = damage;
   }
   public float getDamage() {
      return this.stats.get(ToolStats.ATTACK_DAMAGE) + this.bonusDamage;
   }

   @Override
   protected void defineSynchedData() {
      entityData.define(ZENITH_PART, new CelestialBladePart(Items.WOODEN_SWORD.builtInRegistryHolder(), 0x594319, 0.125, Mth.HALF_PI * 0.5f, 1.75, 1));
      entityData.define(OWNER_ID, -1);
      entityData.define(VARIETY, 0);
      entityData.define(DELAY, 0);
      entityData.define(ROTATION, 0F);
      entityData.define(PITCH, 0F);
      entityData.define(TARGET_POS, BlockPos.ZERO);
      entityData.define(FAKE, false);
      entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
      entityData.define(SPEED,0f);
   }

   @Override
   public boolean ignoreExplosion() {
      return true;
   }

   public void faceTarget(){
      this.faceEntity(this.getTargetPos());
      Vec3 vec = new Vec3(getTargetPos().getX() - getX(), getTargetPos().getY() - getY(), getTargetPos().getZ() - getZ())
              .normalize();
      this.setDeltaMovement(vec.x * getSpeed(), vec.y * getSpeed(), vec.z * getSpeed());
   }

   @Override
   public void tick() {
      if (getDelay() > 0) {
         setDelay(getDelay() - 1);
         return;
      }

      if (this.tickCount >= LIVE_TICKS)
         discard();

      if(getFake()) {
         this.setDeltaMovement(0D,0D,0D);
         return;
      }

      if (!getFake() && !level().isClientSide && (thrower == null || !(thrower instanceof Player) || !thrower.isAlive())) {
         discard();
         return;
      }



      super.tick();

      if (thrower instanceof Player player) {
         if(!level().isClientSide && !getFake() && this.tickCount % 6 == 0) {
            PhantomSwordEntity entity = new PhantomSwordEntity(level());
            entity.thrower = this.thrower;
            entity.setTool(this.toolStack.createStack());
            entity.setFake(true);
            entity.setZenithPart(this.getZenithPart());
            entity.setRotation(this.getRotation());
            entity.setPitch(this.getPitch());
            entity.setPos(getX(), getY(), getZ());
            entity.setVariety(getVariety());
            level().addFreshEntity(entity);
         }

         if (!level().isClientSide) {
            float baseDamage = 10F;
            if (toolStack != null) {
               baseDamage = stats.getInt(ToolStats.ATTACK_DAMAGE);
            }

            AABB axis = new AABB(getX(), getY(), getZ(), xOld, yOld, zOld).inflate(2);
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, axis);
            for (LivingEntity living : entities) {
               if (living == thrower)
                  continue;
               if(living instanceof Animal)
                  continue;
               if (living.invulnerableTime <= 5&&thrower instanceof LivingEntity attacker) {
                  if (toolStack != null) {
                     ProjectileUtils.attackEntity(
                             this.itemStack.getItem(),
                             this,
                             this.toolStack,
                             attacker,
                             living,
                             false
                     );
                  } else {
                     living.hurt(living.damageSources().playerAttack(player), baseDamage * 0.6F);
                  }
                  living.hurt(living.damageSources().magic(), baseDamage * 0.4F);
               }
            }
         }
      }
   }

   @Override
   protected void onHit(HitResult result) {

   }

   public void faceEntity(BlockPos target) {
      double d0 = target.getX() - this.getX();
      double d2 = target.getZ() - this.getZ();
      double d1 = target.getY() - this.getY();

      double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
      float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
      float f1 = (float) (-(Mth.atan2(d1, d3) * (180D / Math.PI)));
      this.setXRot(updateRotation(this.getXRot(), f1, 360F));
      this.setYRot(updateRotation(this.getYRot(), f, 360F));

      setPitch(-this.getXRot());
      setRotation(Mth.wrapDegrees(-this.getYRot() + 180));
   }

   private float updateRotation(float angle, float targetAngle, float maxIncrease) {
      float f = Mth.wrapDegrees(targetAngle - angle);

      if (f > maxIncrease) {
         f = maxIncrease;
      }

      if (f < -maxIncrease) {
         f = -maxIncrease;
      }

      return angle + f;
   }

   @Override
   protected void onHitEntity(EntityHitResult result) {
      super.onHitEntity(result);
      Entity target = result.getEntity();
      Entity player = this.getOwner();
      if (player != null && target instanceof LivingEntity entity) {
         ((LivingEntity)player).setLastHurtByMob(entity);
      }

      LivingEntity livingOwner = this.getOwner() instanceof LivingEntity ? (LivingEntity)this.getOwner() : null;
      if (!this.level().isClientSide()) {
         if (toolStack != null && !this.toolStack.isBroken()){
            ProjectileUtils.attackEntity(
                    this.itemStack.getItem(),
                    this,
                    this.toolStack,
                    livingOwner,
                    target,
                    false
            );
         }
         this.discard();
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      if (this.itemStack != null) {
         tag.put("ToolStack", this.itemStack.serializeNBT());
      }
      if (thrower != null) {
         tag.putUUID(TAG_OWNER, thrower.getUUID());
      }
      tag.putFloat("BonusDamage", bonusDamage);
      tag.putBoolean("Critical", critical);
      tag.putInt(TAG_VARIETY, getVariety());
      tag.putInt(TAG_DELAY, getDelay());
      tag.putFloat(TAG_ROTATION, getRotation());
      tag.putFloat(TAG_PITCH, getPitch());
      tag.putLong(TAG_TARGETPOS, getTargetPos().asLong());
      tag.putBoolean(TAG_FAKE, getFake());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("ToolStack")) {
         this.itemStack = ItemStack.of(tag.getCompound("ToolStack"));
         this.toolStack = ToolStack.from(this.itemStack);
      }
      this.bonusDamage = tag.getFloat("BonusDamage");
      this.critical = tag.getBoolean("Critical");
      this.setVariety(tag.getInt(TAG_VARIETY));
      this.setDelay(tag.getInt(TAG_DELAY));
      this.setRotation(tag.getFloat(TAG_ROTATION));
      this.setPitch(tag.getFloat(TAG_PITCH));
      this.setTargetPos(BlockPos.of(tag.getLong(TAG_TARGETPOS)));
      this.setFake(tag.getBoolean(TAG_FAKE));
   }


   public int getVariety() {
      return entityData.get(VARIETY);
   }

   public void setVariety(int var) {
      entityData.set(VARIETY, var);
   }

   public int getDelay() {
      return entityData.get(DELAY);
   }

   public void setDelay(int var) {
      entityData.set(DELAY, var);
   }

   public float getRotation() {
      return entityData.get(ROTATION);
   }

   public void setRotation(float rot) {
      entityData.set(ROTATION, rot);
   }

   public float getPitch() {
      return entityData.get(PITCH);
   }

   public void setPitch(float rot) {
      entityData.set(PITCH, rot);
   }

   public boolean getFake() {
      return entityData.get(FAKE);
   }

   public void setFake(boolean rot) {
      entityData.set(FAKE, rot);
   }

   public BlockPos getTargetPos() {
      return entityData.get(TARGET_POS);
   }

   public void setTargetPos(BlockPos pos) {
      entityData.set(TARGET_POS, pos);
   }

   @Nonnull
   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
   @Override
   public void writeSpawnData(FriendlyByteBuf buffer) {
      buffer.writeItem(this.getItemStack());
      buffer.writeFloat(this.getRotation());
      buffer.writeFloat(this.getPitch());
      buffer.writeBoolean(this.getFake());
      buffer.writeInt(this.getDelay());
   }

   @Override
   public void readSpawnData(FriendlyByteBuf buffer) {
      this.setItemStack(buffer.readItem());
      this.setRotation(buffer.readFloat());
      this.setPitch(buffer.readFloat());
      this.setFake(buffer.readBoolean());
      this.setDelay(buffer.readInt());
   }
}