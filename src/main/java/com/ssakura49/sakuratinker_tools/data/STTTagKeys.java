package com.ssakura49.sakuratinker_tools.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierManager;

public class STTTagKeys {
   public static class Fluids {
      private static TagKey<Fluid> forgeTag(String name) {
         return TagKey.create(ForgeRegistries.FLUIDS.getRegistryKey(), ResourceLocation.fromNamespaceAndPath("forge", name));
      }
   }

   public static class Items {
      private static TagKey<Item> forgeTag(String name) {
         return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), ResourceLocation.fromNamespaceAndPath("forge", name));
      }

      private static TagKey<Item> stTag(String name) {
         return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), ResourceLocation.fromNamespaceAndPath("sakuratinker", name));
      }
   }

   public static class Modifiers {
      public static final TagKey<Modifier> SPECIAL_TOOL = stTag("special_tool");

      private static TagKey<Modifier> stTag(String name) {
         return ModifierManager.getTag(ResourceLocation.fromNamespaceAndPath("sakuratinker", name));
      }
   }
}
