package com.ssakura49.sakuratinker_tools;

import com.ssakura49.sakura_library.api.client.creativetab.SideBarCategory;
import com.ssakura49.sakura_library.api.client.creativetab.TabSideBarManager;
import com.ssakura49.sakuratinker_tools.compat.CompatManager;
import com.ssakura49.sakuratinker_tools.content.capability.ToolBulletSlotCapability;
import com.ssakura49.sakuratinker_tools.init.*;

import java.util.function.Supplier;

import com.ssakura49.sakuratinker_tools.network.PacketHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

@EventBusSubscriber(
   modid = "sakuratinker_tools",
   bus = Bus.MOD
)
public class STTModule {
   public static void init(IEventBus bus) {
      STTEntityDataSerializers.ENTITY_DATA_SERIALIZERS.register(bus);
      STTEntities.ENTITIES.register(bus);
      STTItems.init(bus);
      STTTags.init();
      STTMenus.MENUS.register(bus);
      STTSound.SOUNDS.register(bus);
      CompatManager.initAll(bus);
      PacketHandler.init();
   }

   @SubscribeEvent
   public static void commonSetup(FMLCommonSetupEvent event) {

       event.enqueueWork(STTMaterialRegistry::init);
       event.enqueueWork(()->
               ToolCapabilityProvider.register(((stack, tool) -> new ToolBulletSlotCapability.Provider(tool)))
       );
   }
   @SubscribeEvent
   public static void clientSetup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         ResourceLocation catTexture = ResourceLocation.fromNamespaceAndPath("sakura_library", "textures/gui/sidebar/sidebar.png");
         ResourceLocation baseTabId = ResourceLocation.fromNamespaceAndPath("sakuratinker", "stcr_tab");
         TabSideBarManager.registerCategory(baseTabId, SideBarCategory.builder().title(Component.translatable("sidebar.sakuratinker.parts")).icon(new Supplier<>() {
             private ItemStack cachedIcon = null;
             public ItemStack get() {
                 if (this.cachedIcon == null || this.cachedIcon.isEmpty()) {
                     MaterialVariantId material;
                     if (MaterialRegistry.isFullyLoaded()) {
                         material = ToolBuildHandler.RANDOM.getMaterial(HeadMaterialStats.ID, RandomSource.create());
                     } else {
                         material = ToolBuildHandler.getRenderMaterial(0);
                     }
                     ItemStack stack = STTItems.swift_blade.get().withMaterial(material);
                     if (!MaterialRegistry.isFullyLoaded()) {
                         return stack;
                     }
                     this.cachedIcon = stack;
                 }
                 return this.cachedIcon;
             }
         }).displayItems(STTTabs::fillParts).build());
         TabSideBarManager.registerCategory(baseTabId, SideBarCategory.builder().title(Component.translatable("sidebar.sakuratinker.casts")).icon(() -> new ItemStack(STTItems.swiftBladeCast.get())).displayItems(STTTabs::fillCasts).build());
         TabSideBarManager.registerCategory(baseTabId, SideBarCategory.builder().title(Component.translatable("sidebar.sakuratinker.tools")).icon(() -> STTItems.swift_sword.get().getRenderTool()).displayItems(STTTabs::fillTools).build());
      });
   }

}
