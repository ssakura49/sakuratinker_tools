package com.ssakura49.sakuratinker_tools.content.entity;

import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectilePierced;
import com.ssakura49.sakuratinker.core.utils.AttackUtil;
import com.ssakura49.sakuratinker_tools.init.STTEntities;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class BulletEntity extends Projectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage, IProjectilePierced {
   public static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.ITEM_STACK);
   private static final EntityDataAccessor<Optional<UUID>> DATA_ATTACKER_UUID = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.OPTIONAL_UUID);

   private ItemStack bulletStack;
   private ToolStack toolStack;
   private StatsNBT stats;
   private float bonusDamage = 0.0F;
   private boolean critical = false;
   private int pierced = 3;
   private final Set<Integer> piercedEntityIds = new IntOpenHashSet();
   private final List<Entity> killedEntities = new ArrayList<>();

   private LivingEntity attacker;

   private static final int MAX_LIFE = 200; // 10秒
   private int lifeTicks = 0;
   @Getter
   private boolean alreadyDamagedOnFire = false;

   public BulletEntity(EntityType<? extends BulletEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.bulletStack = ItemStack.EMPTY;
      this.toolStack = null;
      this.stats = StatsNBT.EMPTY;
   }

   public BulletEntity(Level level, LivingEntity entity) {
      super(STTEntities.BULLET.get(), level);
      this.setOwner(entity);
      this.setAttacker(entity);
      this.bulletStack = ItemStack.EMPTY;
      this.toolStack = null;
      this.stats = StatsNBT.EMPTY;
   }

   public BulletEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
      super(STTEntities.BULLET.get(), level);
   }

   public void setTool(ItemStack tool) {
      if (!tool.isEmpty()) {
         this.bulletStack = tool.copy();
         this.toolStack = ToolStack.from(tool);
         this.stats = this.toolStack.getStats();
      } else {
         this.bulletStack = ItemStack.EMPTY;
         this.toolStack = null;
         this.stats = StatsNBT.EMPTY;
      }
   }
   public ToolStack getTool() {
      return this.toolStack;
   }

   public void setAttacker(LivingEntity attacker) {
      this.attacker = attacker;
      if (attacker != null) this.entityData.set(DATA_ATTACKER_UUID, Optional.of(attacker.getUUID()));
      this.setOwner(attacker);
   }

   public LivingEntity getAttacker() {
      if (attacker == null) {
         UUID uuid = this.entityData.get(DATA_ATTACKER_UUID).orElse(null);
         if (uuid != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity instanceof LivingEntity living) attacker = living;
         }
      }
      return attacker;
   }

   @Override
   protected void defineSynchedData() {
      this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
      this.entityData.define(DATA_ATTACKER_UUID, Optional.empty());
   }

   @Override
   public void setPierced(int pierced) {
      this.pierced = pierced;
   }
   @Override
   public float getPierced() {
      return pierced;
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
      return bonusDamage;
   }


   @Override
   public void tick() {
      super.tick();

      lifeTicks++;
      if (!this.level().isClientSide && lifeTicks > MAX_LIFE) {
         this.discard();
         return;
      }
      if (!this.level().isClientSide && (this.isRemoved() || !this.isAlive())) {
         return;
      }

      Vec3 motion = this.getDeltaMovement();
      Vec3 startPos = this.position();
      Vec3 endPos = startPos.add(motion);
      BlockHitResult blockHit = this.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
      if (blockHit.getType() != HitResult.Type.MISS) {
         endPos = blockHit.getLocation();
      }
      EntityHitResult entityHit = findEntityHit(startPos, endPos);
      if (entityHit != null) {
         this.onHitEntity(entityHit);
      } else if (blockHit.getType() != HitResult.Type.MISS) {
         this.onHitBlock(blockHit);
      }

      this.setPos(endPos);
      this.setDeltaMovement(motion);
      this.setNoGravity(true);
   }

   private EntityHitResult findEntityHit(Vec3 start, Vec3 end) {
      return ProjectileUtil.getEntityHitResult(
              this.level(),
              this,
              start,
              end,
              this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(0.5),
              (entity) -> !this.piercedEntityIds.contains(entity.getId()) && entity.isPickable() && entity != this.getOwner()
      );
   }

   @Override
   protected void onHitEntity(EntityHitResult hitResult) {
      Entity target = hitResult.getEntity();
      if (target instanceof LivingEntity living) {
         this.piercedEntityIds.add(target.getId());
         LivingEntity attacker = getAttacker();
         if (attacker!= null) {
            target.invulnerableTime = 0;
            AttackUtil.attackEntity(
                    toolStack,
                    (LivingEntity) getOwner(),
                    InteractionHand.MAIN_HAND,
                    living,
                    () ->1.0,
                    false,
                    EquipmentSlot.MAINHAND,
                    true,
                    getDamage(),
                    false
            );
         }
//            ProjectileUtils.attackEntity(
//                    bulletStack.getItem(),
//                    this,
//                    toolStack,
//                    (LivingEntity)getOwner(),
//                    target,
//                    false
//            );
         if (living.isDeadOrDying()) {
            this.killedEntities.add(living);
         }
      }

      // 配置可以穿透性
      if (this.piercedEntityIds.size() > getPierced()) {
         this.discard();
      }
   }

   @Override
   protected void onHitBlock(BlockHitResult hitResult) {
      super.onHitBlock(hitResult);
      this.discard();
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.put("Tool", bulletStack.serializeNBT());
      tag.putFloat("BonusDamage", bonusDamage);
      tag.putBoolean("Critical", critical);
      tag.putInt("Pierced", pierced);
      LivingEntity attacker = getAttacker();
      if (attacker != null) {
         tag.putUUID("AttackerUUID", attacker.getUUID());
      }
   }
   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.setTool(ItemStack.of(tag.getCompound("Tool")));
      this.bonusDamage = tag.getFloat("BonusDamage");
      this.critical = tag.getBoolean("Critical");
      this.pierced = tag.getInt("Pierced");
      if (tag.hasUUID("AttackerUUID") && this.level() instanceof ServerLevel serverLevel) {
         Entity entity = serverLevel.getEntity(tag.getUUID("AttackerUUID"));
         if (entity instanceof LivingEntity living) this.attacker = living;
      }
   }

   @Override
   public void writeSpawnData(FriendlyByteBuf buffer) {
      buffer.writeItem(this.bulletStack);
      buffer.writeFloat(this.bonusDamage);
      buffer.writeBoolean(this.critical);
      buffer.writeInt(this.pierced);
      buffer.writeBoolean(getAttacker() != null);
      if (getAttacker() != null) {
         buffer.writeUUID(getAttacker().getUUID());
      }
   }
   @Override
   public void readSpawnData(FriendlyByteBuf buffer) {
      this.setTool(buffer.readItem());
      this.bonusDamage = buffer.readFloat();
      this.critical = buffer.readBoolean();
      this.pierced = buffer.readInt();
      boolean hasAttacker = buffer.readBoolean();
      if (hasAttacker) {
         UUID uuid = buffer.readUUID();
         if (this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity instanceof LivingEntity living) this.attacker = living;
         } else {
            this.entityData.set(DATA_ATTACKER_UUID, Optional.of(uuid));
         }
      }
   }

   @Override
   public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   public void markFired() {
      this.alreadyDamagedOnFire = true;
   }

}
