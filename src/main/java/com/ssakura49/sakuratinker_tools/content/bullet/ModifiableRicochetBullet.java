package com.ssakura49.sakuratinker_tools.content.bullet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class ModifiableRicochetBullet extends AbstractModifiableBullet {
   private int ricochetCount = 0;
   private static final int MAX_RICOCHET_COUNT = 5;
   private final float projectileSpeed;

   public ModifiableRicochetBullet(
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
      this.projectileSpeed = projectileSpeed;
   }

   @Override
   public boolean tick() {
      if (this.isValid()) {
         return false;
      } else {
         this.applyPhysics();
         Vec3 nextPos = this.position.add(this.deltaMovement);
         double stepDistance = this.deltaMovement.length();
         if (this.checkEntityCollisions(this.position, nextPos)) {
            return false;
         } else {
            BlockHitInfo blockHit = this.checkBlockCollision(this.position, nextPos);
            this.spawnParticles(this.position, blockHit.particleEndPos);
            if (blockHit.shouldStop) {
               return false;
            } else {
               this.position = nextPos;
               this.distanceTraveled += stepDistance;
               return this.distanceTraveled < this.maxRange;
            }
         }
      }
   }

   @Override
   protected BlockHitInfo checkBlockCollision(Vec3 from, Vec3 to) {
      if (this.ricochetCount >= MAX_RICOCHET_COUNT) {
         return super.checkBlockCollision(from, to);
      } else {
         HitResult hit = this.level.clip(new ClipContext(from, to, Block.COLLIDER, Fluid.NONE, this.shooter));
         if (hit.getType() == Type.MISS) {
            return new BlockHitInfo(to, to, false);
         } else {
            this.ricochet((BlockHitResult)hit);
            Vec3 hitLocation = hit.getLocation();
            return new BlockHitInfo(hitLocation, hitLocation, false);
         }
      }
   }

   private void ricochet(BlockHitResult hitResult) {
      Vec3 incident = this.deltaMovement.normalize();
      Vec3 normal = new Vec3(hitResult.getDirection().getStepX(), hitResult.getDirection().getStepY(), hitResult.getDirection().getStepZ());
      double dotProduct = incident.dot(normal);
      Vec3 reflected = incident.subtract(normal.scale(2.0 * dotProduct));
      this.deltaMovement = reflected.scale(this.projectileSpeed);
      this.position = hitResult.getLocation().add(reflected.scale(0.1));
      this.ricochetCount++;
   }

   @Override
   protected boolean checkEntityCollisions(Vec3 from, Vec3 to) {
      AABB checkBox = new AABB(from, to).inflate(0.5);
      List<Entity> entities = this.level.getEntities(this.shooter, checkBox, e -> e instanceof LivingEntity && !e.isSpectator() && e.isPickable());
      entities.sort((e1, e2) -> Double.compare(e1.distanceToSqr(from), e2.distanceToSqr(from)));

      for (Entity entity : entities) {
         LivingEntity target = (LivingEntity)entity;
         if (!this.hitEntities.contains(target.getId())) {
            AABB targetBox = target.getBoundingBox().inflate(this.projectileRadius);
            if (targetBox.clip(from, to).isPresent() && this.hitEntity(target, false)) {
               this.hitEntities.add(target.getId());
               this.onEntityHit(target);
               this.damage = this.damage * (1.0F - this.penetrationDrop);
               boolean reachedPenetrationLimit = this.hitCount >= MAX_HITS || this.damage < 0.5F;
               if (reachedPenetrationLimit) {
                  if (this.ricochetCount >= MAX_RICOCHET_COUNT) {
                     return true;
                  }
                  break;
               }
            }
         }
      }

      return false;
   }
}
