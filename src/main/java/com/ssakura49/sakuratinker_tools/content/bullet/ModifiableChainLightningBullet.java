package com.ssakura49.sakuratinker_tools.content.bullet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ModifiableChainLightningBullet extends AbstractModifiableBullet {
   private static final double CHAIN_RANGE = 3.0;
   private static final int MAX_CHAIN_TARGETS = 5;
   private static final float CHAIN_DAMAGE_MULTIPLIER = 0.8F;
   private static final int DAMAGE_DELAY = 20;
   private final Queue<LivingEntity> chainTargets = new LinkedList<>();
   private int delayCounter = 0;

   public ModifiableChainLightningBullet(
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
   protected void onEntityHit(LivingEntity entity) {
      super.onEntityHit(entity);
      this.findChainTargets(entity);
      this.applyNextChainDamage();
   }

   private void findChainTargets(LivingEntity primaryTarget) {
      AABB searchBox = primaryTarget.getBoundingBox().inflate(CHAIN_RANGE);
      List<LivingEntity> nearbyEntities = this.level.getEntitiesOfClass(
         LivingEntity.class,
         searchBox,
         entity -> entity != primaryTarget && entity != this.shooter && entity.isAlive() && !this.chainTargets.contains(entity)
      );
      Collections.shuffle(nearbyEntities);
      int targetsToAdd = Math.min(MAX_CHAIN_TARGETS, nearbyEntities.size());

      for (int i = 0; i < targetsToAdd; i++) {
         this.chainTargets.offer(nearbyEntities.get(i));
      }
   }

   private void applyNextChainDamage() {
      if (!this.chainTargets.isEmpty()) {
         LivingEntity nextTarget = this.chainTargets.poll();
         this.damage *= CHAIN_DAMAGE_MULTIPLIER;
         super.hitEntity(nextTarget);
      }
   }

   @Override
   public boolean tick() {
      boolean continueBullet = super.tick();
      if (!this.chainTargets.isEmpty() && this.delayCounter >= DAMAGE_DELAY) {
         this.delayCounter = 0;
         this.applyNextChainDamage();
      } else if (!this.chainTargets.isEmpty()) {
         this.delayCounter++;
      }

      return continueBullet || !this.chainTargets.isEmpty();
   }
}
