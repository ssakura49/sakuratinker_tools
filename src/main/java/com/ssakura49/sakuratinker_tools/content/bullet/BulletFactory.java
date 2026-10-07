package com.ssakura49.sakuratinker_tools.content.bullet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

/**
 * 子弹创建入口{@link BulletRegistry}。
 * {@link BulletType} 枚举保留为内置弹种 id 的常量来源，与注册表的字符串 id 一一对应；
 * 自定义弹种直接 {@link BulletRegistry#register}，然后枪的 {@code getBulletType()} 返回该 id 即可。
 */
public final class BulletFactory {
   private BulletFactory() {
   }

   public static IBullet createBullet(
      BulletType type,
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
      return BulletRegistry.create(
         type.name(), level, shooter, tool, start, direction, damage, penetrationDrop, isCrit,
         projectileSpeed, gravityPerTick, falloffStart, falloffEnd
      );
   }

   public static IBullet createSimple(Level level, LivingEntity shooter, Vec3 start, Vec3 direction, float damage) {
      return BulletRegistry.create(
         BulletType.NORMAL.name(), level, shooter, null, start, direction,
         damage, 0.3F, false, 5.0F, 0.05F, 20.0F, 100.0F
      );
   }

   public enum BulletType {
      NORMAL,
      EXPLOSIVE,
      PIERCING,
      RICOCHET,
      CHAIN_LIGHTNING
   }
}
