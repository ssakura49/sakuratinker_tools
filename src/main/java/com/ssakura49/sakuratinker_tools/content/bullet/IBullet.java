package com.ssakura49.sakuratinker_tools.content.bullet;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 由 {@link BulletManager} 在每个 LevelTick 推进 {@link #tick()}，返回 false 表示该子弹消失
 */
public interface IBullet {
   boolean tick();

   Vec3 getPosition();

   Level getLevel();

   double getDistanceTraveled();

   boolean isValid();
}
