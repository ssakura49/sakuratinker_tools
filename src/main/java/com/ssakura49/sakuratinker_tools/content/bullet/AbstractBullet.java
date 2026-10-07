package com.ssakura49.sakuratinker_tools.content.bullet;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * 弹道：每 tick 施加重力、步进、方块扫掠、实体防重复命中、穿透衰减、距离衰减，超出最大射程或命中方块作废。
 * 伤害：直接 {@link LivingEntity#hurt}，自带无敌帧处理与暴击增强。
 * 可改造的有耐久子弹继承 {@link AbstractModifiableBullet}（命中走AttackUtil）。
 */
public abstract class AbstractBullet implements IBullet {
   protected final Level level;
   protected final LivingEntity shooter;
   protected Vec3 position;
   protected Vec3 deltaMovement;
   protected float damage;
   protected final float penetrationDrop;
   protected final boolean isCrit;
   protected double distanceTraveled;
   protected int hitCount = 0;
   protected BlockPos lastHitBlockPos = null;
   protected final IntSet hitEntities = new IntOpenHashSet();
   protected final double gravityPerTick;
   protected final float falloffStart;
   protected final float falloffEnd;
   protected final double projectileRadius;
   protected final double maxRange;
   protected static final int MAX_HITS = 3;

   public AbstractBullet(
      Level level,
      LivingEntity shooter,
      Vec3 start,
      Vec3 direction,
      float damage,
      float penetrationDrop,
      boolean isCrit,
      float projectileSpeed,
      float gravityPerTick,
      float falloffStart,
      float falloffEnd
   ) {
      this.level = level;
      this.shooter = shooter;
      this.position = start;
      this.deltaMovement = direction.normalize().scale(projectileSpeed);
      this.damage = damage;
      this.penetrationDrop = penetrationDrop;
      this.isCrit = isCrit;
      this.distanceTraveled = 0.0;
      this.gravityPerTick = gravityPerTick;
      this.falloffStart = falloffStart;
      this.falloffEnd = falloffEnd;
      this.projectileRadius = 0.1;
      this.maxRange = Math.max(100.0, Math.max(falloffEnd, projectileSpeed * 6.0F));
   }

   @Override
   public boolean tick() {
      if (this.isValid()) {
         return false;
      } else {
         this.applyPhysics();
         Vec3 nextPos = this.position.add(this.deltaMovement);
         double stepDistance = this.deltaMovement.length();
         BlockHitInfo blockHit = this.checkBlockCollision(this.position, nextPos);
         this.spawnParticles(this.position, blockHit.particleEndPos);
         if (this.checkEntityCollisions(this.position, nextPos)) {
            return false;
         } else if (blockHit.shouldStop) {
            return false;
         } else {
            this.position = nextPos;
            this.distanceTraveled += stepDistance;
            return this.distanceTraveled < this.maxRange;
         }
      }
   }

   protected void applyPhysics() {
      this.deltaMovement = this.deltaMovement.add(0.0, -this.gravityPerTick, 0.0);
   }

   /** 方块碰撞；可穿透方块时由子类覆写 {@link #canPenetrateBlock} */
   protected BlockHitInfo checkBlockCollision(Vec3 from, Vec3 to) {
      BlockHitResult hit = this.level.clip(new ClipContext(from, to, Block.COLLIDER, Fluid.NONE, this.shooter));
      if (hit.getType() == Type.MISS) {
         return new BlockHitInfo(to, to, false);
      }
      BlockPos hitPos = hit.getBlockPos();
      BlockState state = this.level.getBlockState(hitPos);
      if (this.canPenetrateBlock(state)) {
         if (!hitPos.equals(this.lastHitBlockPos)) {
            this.damage *= 0.5F;
            this.lastHitBlockPos = hitPos;
         }
         return new BlockHitInfo(to, to, false);
      }
      Vec3 hitLoc = hit.getLocation();
      return new BlockHitInfo(hitLoc, hitLoc, true);
   }

   /** 是否可穿透该方块（普通子弹默认不能）；可改造子弹按工具挖掘等级穿透 */
   protected boolean canPenetrateBlock(BlockState state) {
      return false;
   }

   protected boolean checkEntityCollisions(Vec3 from, Vec3 to) {
      AABB checkBox = new AABB(from, to).inflate(0.5);
      List<Entity> entities = this.level.getEntities(this.shooter, checkBox, e -> e instanceof LivingEntity && !e.isSpectator() && e.isPickable());
      entities.sort((e1, e2) -> Double.compare(e1.distanceToSqr(from), e2.distanceToSqr(from)));

      for (Entity entity : entities) {
         LivingEntity target = (LivingEntity)entity;
         if (!this.hitEntities.contains(target.getId())) {
            AABB targetBox = target.getBoundingBox().inflate(this.projectileRadius);
            if (targetBox.clip(from, to).isPresent() && this.hitEntity(target)) {
               this.hitEntities.add(target.getId());
               this.onEntityHit(target);
               return true;
            }
         }
      }

      return false;
   }

   /** 单发命中：普通子弹直接 hurt（无敌帧处理 + 暴击增强）。可改造子弹由 AbstractModifiableBullet 覆写走 AttackUtil。 */
   protected boolean hitEntity(LivingEntity target) {
      boolean applyFalloff = !(this.isExplosive());
      return this.hitEntity(target, applyFalloff);
   }

   protected boolean hitEntity(LivingEntity target, boolean applyFalloff) {
      float actualDamage = applyFalloff ? this.damage * this.computeDistanceMultiplier(this.distanceTraveled) : this.damage;
      if (actualDamage <= 0.5F) {
         return false;
      } else {
         if (this.isCrit) {
            actualDamage *= 1.5F;
         }

         boolean success = this.dealDamageTo(target, actualDamage, false);
         if (success) {
            this.hitCount++;
            if (this.isCrit) {
               this.level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, target.getSoundSource(), 1.0F, 1.0F);
            }
         }
         return success;
      }
   }

   /** 施加伤害的统一入口（可被抽象子弹覆写以使用 AttackUtil 等替代伤害管线）。 */
   protected boolean dealDamageTo(LivingEntity target, float damage, boolean useExplosion) {
      int previousInvuln = target.invulnerableTime;
      target.invulnerableTime = 0;
      DamageSource source;
      if (useExplosion) {
         source = this.level.damageSources().explosion(null, this.shooter);
      } else if (this.shooter instanceof Player player) {
         source = this.level.damageSources().playerAttack(player);
      } else {
         source = this.level.damageSources().mobAttack(this.shooter);
      }
      boolean success = target.hurt(source, damage);
      target.invulnerableTime = previousInvuln;
      return success;
   }

   /** 命中后的回调（链电/爆炸等弹种用） */
   protected void onEntityHit(LivingEntity target) {
   }

   /** 是否爆炸型（爆炸弹为 true，命中时不做距离衰减） */
   protected boolean isExplosive() {
      return false;
   }

   protected void spawnParticles(Vec3 from, Vec3 to) {
      if (this.level instanceof ServerLevel serverLevel) {
         double dist = from.distanceTo(to);
         Vec3 dir = to.subtract(from).normalize();

         for (double d = 0.0; d < dist; d += 2.0) {
            Vec3 p = from.add(dir.scale(d));
            serverLevel.sendParticles(ParticleTypes.CRIT, p.x, p.y, p.z, 1, 0.0, 0.0, 0.0, 0.0);
         }
      }
   }

   /** 距离衰减：falloffStart 内 100%，falloffStart 到 falloffEnd 线性降到 0 */
   protected float computeDistanceMultiplier(double distance) {
      if (distance <= this.falloffStart) {
         return 1.0F;
      } else if (this.falloffEnd <= this.falloffStart) {
         return 0.0F;
      } else {
         double clamped = Math.min(distance, this.falloffEnd);
         double fraction = (clamped - this.falloffStart) / (this.falloffEnd - this.falloffStart);
         return (float)Math.max(0.0, 1.0 - fraction);
      }
   }

   @Override
   public Vec3 getPosition() {
      return this.position;
   }

   @Override
   public Level getLevel() {
      return this.level;
   }

   @Override
   public double getDistanceTraveled() {
      return this.distanceTraveled;
   }

   @Override
   public boolean isValid() {
      return this.shooter == null || !this.shooter.isAlive();
   }

   protected static class BlockHitInfo {
      public final Vec3 nextPos;
      public final Vec3 particleEndPos;
      public final boolean shouldStop;

      public BlockHitInfo(Vec3 nextPos, Vec3 particleEndPos, boolean shouldStop) {
         this.nextPos = nextPos;
         this.particleEndPos = particleEndPos;
         this.shouldStop = shouldStop;
      }
   }
}
