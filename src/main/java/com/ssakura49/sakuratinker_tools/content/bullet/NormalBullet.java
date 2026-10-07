package com.ssakura49.sakuratinker_tools.content.bullet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NormalBullet extends AbstractBullet {
   public NormalBullet(
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
      super(level, shooter, start, direction, damage, penetrationDrop, isCrit, projectileSpeed, gravityPerTick, falloffStart, falloffEnd);
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
            if (targetBox.clip(from, to).isPresent() && this.hitEntity(target)) {
               this.hitEntities.add(target.getId());
               this.onEntityHit(target);
               this.damage = this.damage * (1.0F - this.penetrationDrop);
               if (this.hitCount >= MAX_HITS || this.damage < 0.5F) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
