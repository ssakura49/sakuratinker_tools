package com.ssakura49.sakuratinker_tools.content.bullet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.LinkedHashMap;
import java.util.Map;


public final class BulletRegistry {
   public interface BulletFactory {
      IBullet create(
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
      );
   }

   public record BulletTypeEntry(BulletFactory plainFactory, BulletFactory modifiableFactory) {
      public static BulletTypeEntry of(BulletFactory plain, BulletFactory modifiable) {
         return new BulletTypeEntry(plain, modifiable);
      }
   }

   private static final Map<String, BulletTypeEntry> REGISTRY = new LinkedHashMap<>();

   private BulletRegistry() {
   }

   public static void register(String id, BulletTypeEntry entry) {
      if (id == null || id.isBlank()) {
         throw new IllegalArgumentException("Bullet id cannot be blank");
      }
      REGISTRY.put(id, entry);
   }

   public static void register(String id, BulletFactory plain, BulletFactory modifiable) {
      register(id, BulletTypeEntry.of(plain, modifiable));
   }

   public static IBullet create(
      String id,
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
      BulletTypeEntry entry = REGISTRY.get(id);
      if (entry == null) {
         throw new IllegalArgumentException("Unknown bullet type id: " + id);
      }
      BulletFactory factory;
      if (tool != null && !tool.isBroken() && entry.modifiableFactory() != null) {
         factory = entry.modifiableFactory();
      } else {
         factory = entry.plainFactory();
         if (factory == null) {
            throw new IllegalArgumentException("Bullet type '" + id + "' does not support plain (non-tool) ammo");
         }
      }
      return factory.create(
         level, shooter, tool, start, direction, damage, penetrationDrop, isCrit,
         projectileSpeed, gravityPerTick, falloffStart, falloffEnd
      );
   }

   public static boolean isRegistered(String id) {
      return REGISTRY.containsKey(id);
   }

   public static final String NORMAL = "normal";
   public static final String EXPLOSIVE = "explosive";
   public static final String PIERCING = "piercing";
   public static final String RICOCHET = "ricochet";
   public static final String CHAIN_LIGHTNING = "chain_lightning";

   static {
      register(NORMAL,
         (level, shooter, tool, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd)
            -> new NormalBullet(level, shooter, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd),
              null
      );
      register(EXPLOSIVE,
         (level, shooter, tool, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd)
            -> new ExplosiveBullet(level, shooter, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd),
              null
      );
      register(PIERCING,
         (level, shooter, tool, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd)
            -> new PiercingBullet(level, shooter, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd),
              null
      );
      register(RICOCHET,
         (level, shooter, tool, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd)
            -> new RicochetBullet(level, shooter, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd),
              null
      );
      register(CHAIN_LIGHTNING,
         (level, shooter, tool, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd)
            -> new ChainLightningBullet(level, shooter, start, direction, damage, penetrationDrop, isCrit, speed, gravity, falloffStart, falloffEnd),
              null
      );
   }
}
