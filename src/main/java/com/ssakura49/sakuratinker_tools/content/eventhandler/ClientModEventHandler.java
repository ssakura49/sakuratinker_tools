package com.ssakura49.sakuratinker_tools.content.eventhandler;

import com.ssakura49.sakuratinker_tools.client.renderer.entity.BulletRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.CelestialBladeEntityRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.GhostKnifeRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.LaserRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.ManaRayRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.MiniGrapplingHookRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.ShurikenRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.TinkerArrowEntityRenderer;
import com.ssakura49.sakuratinker_tools.client.renderer.entity.YoyoRenderer;
import com.ssakura49.sakuratinker_tools.init.STTEntities;
import java.util.Collection;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import slimeknights.tconstruct.gadgets.client.FancyItemFrameRenderer;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class ClientModEventHandler {
   @SubscribeEvent
   static void registerModels(ModelEvent.RegisterAdditional event) {
      Collection<ResourceLocation> locations = FancyItemFrameRenderer.LOCATIONS_MODEL.values();
      Objects.requireNonNull(event);
      Objects.requireNonNull(event);
      locations.forEach(event::register);
      locations = FancyItemFrameRenderer.LOCATIONS_MODEL_MAP.values();
      Objects.requireNonNull(event);
      Objects.requireNonNull(event);
      locations.forEach(event::register);
   }

   @SubscribeEvent
   public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
      event.registerEntityRenderer(STTEntities.GHOST_KNIFE.get(), GhostKnifeRenderer::new);
      event.registerEntityRenderer(STTEntities.TINKER_ARROW_ENTITY.get(), TinkerArrowEntityRenderer::new);
      event.registerEntityRenderer(STTEntities.MINI_GRAPPLING_HOOK.get(), MiniGrapplingHookRenderer::new);
      event.registerEntityRenderer(STTEntities.LASER_PROJECTILE.get(), LaserRenderer::new);

      event.registerEntityRenderer(STTEntities.MANA_RAY.get(), ManaRayRenderer::new);
      event.registerEntityRenderer(STTEntities.YOYO.get(), YoyoRenderer::new);
      event.registerEntityRenderer(STTEntities.BULLET.get(), BulletRenderer::new);
      event.registerEntityRenderer(STTEntities.CELESTIAL_BLADE_ENTITY.get(), CelestialBladeEntityRenderer::new);
      event.registerEntityRenderer(STTEntities.SHURIKEN_ENTITY.get(), ShurikenRenderer::new);
   }
}
