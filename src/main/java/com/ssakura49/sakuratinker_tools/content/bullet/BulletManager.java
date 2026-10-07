package com.ssakura49.sakuratinker_tools.content.bullet;

import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


@Mod.EventBusSubscriber(modid = SakuraTinkerTools.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BulletManager {
   private static final ConcurrentLinkedQueue<IBullet> pendingBullets = new ConcurrentLinkedQueue<>();
   private static final List<IBullet> bullets = new ArrayList<>();

   public static void addBullet(IBullet bullet) {
      pendingBullets.add(bullet);
   }

   public static void spawnBullet(
      String type,
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
      IBullet bullet = BulletRegistry.create(
         type, level, shooter, tool, start, direction, damage, penetrationDrop, isCrit,
         projectileSpeed, gravityPerTick, falloffStart, falloffEnd
      );
      addBullet(bullet);
   }

   @SubscribeEvent
   public static void onLevelTick(LevelTickEvent event) {
      if (event.phase == Phase.END && !event.level.isClientSide()) {
         while (!pendingBullets.isEmpty()) {
            bullets.add(pendingBullets.poll());
         }

         Iterator<IBullet> iterator = bullets.iterator();
         while (iterator.hasNext()) {
            IBullet bullet = iterator.next();
            if (bullet.getLevel() == event.level && !bullet.tick()) {
               iterator.remove();
            }
         }
      }
   }

   @SubscribeEvent
   public static void onServerStopping(ServerStoppingEvent event) {
      bullets.clear();
      pendingBullets.clear();
   }

   public static int getActiveBulletCount() {
      return bullets.size();
   }
}
