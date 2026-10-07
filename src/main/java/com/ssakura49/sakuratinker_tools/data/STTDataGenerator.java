package com.ssakura49.sakuratinker_tools.data;

import com.ssakura49.sakuratinker_tools.data.providers.*;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.material.GeneratorPartTextureJsonGenerator;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;

@EventBusSubscriber(
   modid = "sakuratinker_tools",
   bus = Bus.MOD
)
public class STTDataGenerator {
   @SubscribeEvent
   public static void gatherData(GatherDataEvent event) {
      DataGenerator generator = event.getGenerator();
      PackOutput output = generator.getPackOutput();
      ExistingFileHelper helper = event.getExistingFileHelper();
      CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
      boolean server = event.includeServer();
      boolean client = event.includeClient();
      generator.addProvider(server, new STTToolDefinitionDataProvider(output));
      generator.addProvider(client, new STTItemModelProvider(output, helper));
      generator.addProvider(server, new STTStationSlotLayoutProvider(output));
      generator.addProvider(client, new STTMaterialProvider(output));
      generator.addProvider(client, new STTMaterialStatProvider(output));
      generator.addProvider(client, new STTMaterialModifierProvider(output));
      generator.addProvider(client, new STTRecipeProvider(output));
      generator.addProvider(client, new STTModifierTagProvider(output, helper));
      generator.addProvider(client, new MaterialPartTextureGenerator(output, helper, new STTPartSpriteProvider(), new AbstractMaterialSpriteProvider[]{new TinkerMaterialSpriteProvider()}));
      generator.addProvider(client, new STTModifierProvider(output));
      var blockTags = new BlockTagsProvider(output, lookupProvider, "sakuratinker", helper) {
         protected void addTags(HolderLookup.@NotNull Provider provider) {
         }
      };
      generator.addProvider(client, blockTags);
      generator.addProvider(client, new STTItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), helper));
      generator.addProvider(client, new STTToolItemModelProvider(output, helper));
      generator.addProvider(client, new GeneratorPartTextureJsonGenerator(output, "sakuratinker", new STTPartSpriteProvider()));
      generator.addProvider(server,new STTEntityTagProvider(output,lookupProvider,helper));
   }
}
