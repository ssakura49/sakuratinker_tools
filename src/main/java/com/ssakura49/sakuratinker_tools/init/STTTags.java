package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class STTTags {
   private static boolean tagsLoaded = false;

   public static void init() {
      Items.init();
      Entities.init();
      MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TagsUpdatedEvent.class, (event) -> tagsLoaded = true);
   }

   public static boolean isTagsLoaded() {
      return tagsLoaded;
   }

   public static class Items {
      public static final TagKey<Item> CURIO_MODIFIABLE = local("modifiable/curio");

      public static void init() {
      }

      private static TagKey<Item> local(String name) {
         return TagKey.create(Registries.ITEM, SakuraTinker.getResource(name));
      }
   }

   public static class Blocks {
   }

   public static class Entities {
      public static final TagKey<EntityType<?>> NO_GRAPPLE = local("no_grapple");

      public static void init() {
      }

      private static TagKey<EntityType<?>> local(String name) {
         return TagKey.create(Registries.ENTITY_TYPE, SakuraTinker.getResource(name));
      }
   }

   public static boolean isNoGrapple(Entity entity) {
      if (!tagsLoaded) return false;
      return entity.getType().is(STTTags.Entities.NO_GRAPPLE);
   }
}
