package com.ssakura49.sakuratinker_tools;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("sakuratinker_tools")
public class SakuraTinkerTools {
   public static final String MODID = "sakuratinker_tools";
   private static final Logger LOGGER = LogUtils.getLogger();

   public static ResourceLocation getResource(String string) {
      return ResourceLocation.fromNamespaceAndPath("sakuratinker_tools", string);
   }

   public SakuraTinkerTools() {
      IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
      MinecraftForge.EVENT_BUS.register(this);
      STTModule.init(modEventBus);
   }

   @SubscribeEvent
   public void onServerStarting(ServerStartingEvent event) {
   }
}
