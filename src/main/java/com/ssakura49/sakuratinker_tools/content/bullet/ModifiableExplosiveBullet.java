package com.ssakura49.sakuratinker_tools.content.bullet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class ModifiableExplosiveBullet extends AbstractModifiableBullet {
   private static final double EXPLOSION_RADIUS = 1.0;

   public ModifiableExplosiveBullet(
      Level level,
      LivingEntity shooter,
      ToolStack tool,
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
      super(level, shooter, tool, start, direction, damage, penetrationDrop, isCrit, projectileSpeed, gravityPerTick, falloffStart, falloffEnd);
   }

   @Override
   protected boolean isExplosive() {
      return true;
   }

   @Override
   protected BlockHitInfo checkBlockCollision(Vec3 from, Vec3 to) {
      HitResult hit = this.level.clip(new ClipContext(from, to, Block.COLLIDER, Fluid.NONE, this.shooter));
      if (hit.getType() != Type.MISS) {
         Vec3 hitLoc = hit.getLocation();
         this.explodeAtPosition(hitLoc);
         return new BlockHitInfo(hitLoc, hitLoc, true);
      }
      return new BlockHitInfo(to, to, false);
   }

   @Override
   protected boolean hitEntity(LivingEntity target) {
      Vec3 hitPos = target.position().add(0.0, target.getBbHeight() * 0.5, 0.0);
      this.explodeAtPosition(hitPos, target.getId());
      return true;
   }

   @Override
   protected void onEntityHit(LivingEntity target) {
   }

   private void explodeAtPosition(Vec3 position, int directHitEntityId) {
      if (!this.level.isClientSide) {
         this.level.playSound(
            null,
            position.x,
            position.y,
            position.z,
            SoundEvents.GENERIC_EXPLODE,
            SoundSource.PLAYERS,
            1.5F,
            (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F
         );
         if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, position.x, position.y, position.z, 2, 0.1, 0.1, 0.1, 0.05);
         }

         float finalDamage = this.damage;
         if (this.isCrit) {
            finalDamage *= 1.5F;
         }
         AABB explosionBox = new AABB(position, position).inflate(EXPLOSION_RADIUS);

         for (Entity entity : this.level.getEntities(this.shooter, explosionBox, e -> !e.isSpectator() && e.isPickable())) {
            if (!this.hitEntities.contains(entity.getId())) {
               if (entity instanceof LivingEntity living) {
                  if (this.dealDamageTo(living, finalDamage, true)) {
                     this.hitEntities.add(entity.getId());
                     double dx = living.getX() - position.x;
                     double dz = living.getZ() - position.z;
                     double distSq = dx * dx + dz * dz;
                     if (distSq > 0.0) {
                        living.push(dx / Math.sqrt(distSq) * 0.4, 0.15, dz / Math.sqrt(distSq) * 0.4);
                     }
                  }
               } else if (entity.hurt(this.level.damageSources().explosion(null, this.shooter), finalDamage)) {
                  this.hitEntities.add(entity.getId());
               }
            }
         }
      }
   }

   private void explodeAtPosition(Vec3 position) {
      this.explodeAtPosition(position, -1);
   }
}
