package com.ssakura49.sakuratinker_tools.data.providers;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.compat.wizardry.init.WizardryItem;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import com.ssakura49.sakuratinker_tools.init.STTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.registration.object.*;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import static slimeknights.tconstruct.common.TinkerTags.Items.*;

public class STTItemTagProvider extends ItemTagsProvider {
   public STTItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> lookup, @Nullable ExistingFileHelper existingFileHelper) {
      super(output, provider, lookup, "sakuratinker", existingFileHelper);
   }

   protected void addTags(HolderLookup.@NotNull Provider provider) {
      var goldCasts = this.tag(TinkerTags.Items.GOLD_CASTS);
      var sandCasts = this.tag(TinkerTags.Items.SAND_CASTS);
      var redSandCasts = this.tag(TinkerTags.Items.RED_SAND_CASTS);

      var multiUseCasts = this.tag(TinkerTags.Items.MULTI_USE_CASTS);
      var singleUseCasts = this.tag(TinkerTags.Items.SINGLE_USE_CASTS);

      for (CastItemObject cast : STTItems.getListCasts()) {
         goldCasts.add(cast.get());
         sandCasts.add(cast.getSand());
         redSandCasts.add(cast.getRedSand());
         addPartCastTags(cast, true, multiUseCasts, singleUseCasts);
      }

      for (CastItemObject cast : ISSItem.getListCasts()) {
         goldCasts.addOptional(cast.getId());
         sandCasts.addOptional(cast.getSand().builtInRegistryHolder().key().location());
         redSandCasts.addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
         addPartCastTags(cast, false, multiUseCasts, singleUseCasts);
      }

      for (CastItemObject cast : GoetyItem.getListCasts()) {
         goldCasts.addOptional(cast.getId());
         sandCasts.addOptional(cast.getSand().builtInRegistryHolder().key().location());
         redSandCasts.addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
         addPartCastTags(cast, false, multiUseCasts, singleUseCasts);
      }

      for (CastItemObject cast : EBItem.getListCasts()) {
         goldCasts.addOptional(cast.getId());
         sandCasts.addOptional(cast.getSand().builtInRegistryHolder().key().location());
         redSandCasts.addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
         addPartCastTags(cast, false, multiUseCasts, singleUseCasts);
      }

      for (CastItemObject cast : WizardryItem.getListCasts()) {
         goldCasts.addOptional(cast.getId());
         sandCasts.addOptional(cast.getSand().builtInRegistryHolder().key().location());
         redSandCasts.addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
         addPartCastTags(cast, false, multiUseCasts, singleUseCasts);
      }


      var toolPartsKey = TinkerTags.Items.TOOL_PARTS;
      var toolPartsTag = this.tag(TinkerTags.Items.TOOL_PARTS);
      for (ItemObject<ToolPartItem> part : STTItems.getListParts()) {
         toolPartsTag.add(part.get());
      }
      for (ItemObject<ToolPartItem> part : ISSItem.getListParts()) {
         toolPartsTag.addOptional(part.getId());
      }
      for (ItemObject<ToolPartItem> part : GoetyItem.getListParts()) {
         toolPartsTag.addOptional(part.getId());
      }
      for (ItemObject<ToolPartItem> part : EBItem.getListParts()) {
         toolPartsTag.addOptional(part.getId());
      }
      for (ItemObject<ToolPartItem> part : WizardryItem.getListParts()) {
         toolPartsTag.addOptional(part.getId());
      }


      this.addToolTags(
              STTItems.tinker_charm.get(),
              MODIFIABLE,BONUS_SLOTS,MULTIPART_TOOL
      );

      this.addToolTags(STTItems.swift_sword.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SWORD,
              SMALL_TOOLS,
              BONUS_SLOTS,
              ItemTags.SWORDS,
              AOE
      );

      this.addToolTags(STTItems.great_sword.get(),
              MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SWORD, BROAD_TOOLS,
              BONUS_SLOTS,
              ItemTags.SWORDS,
              AOE
      );
      this.addToolTags(STTItems.vampire_knife.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SWORD,
              SMALL_TOOLS,
              BONUS_SLOTS,
              ItemTags.SWORDS,
              AOE
      );
      this.addToolTags(STTItems.blade_convergence.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SWORD,
              BROAD_TOOLS,
              BONUS_SLOTS,
              ItemTags.SWORDS,
              AOE
      );
      this.addToolTags(STTItems.scythe.get(),
              MULTIPART_TOOL, DURABILITY, HARVEST_PRIMARY, MELEE_PRIMARY, INTERACTABLE_RIGHT, AOE, BROAD_TOOLS, BONUS_SLOTS, ItemTags.HOES
      );
      this.addToolTags(STTItems.laser_gun.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              INTERACTABLE_RIGHT,
              SWORD,
              SMALL_TOOLS,
              BONUS_SLOTS
      );
      this.addToolTags(STTItems.revolver.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              INTERACTABLE_RIGHT,
              SWORD,
              SMALL_TOOLS,
              BONUS_SLOTS,
              INTERACTABLE_CHARGE
      );

      this.addToolTags(STTItems.tinker_bullet.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SMALL_TOOLS,
              BONUS_SLOTS,
              AOE
      );
      this.addToolTags(STTItems.tinker_arrow.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SMALL_TOOLS,
              BONUS_SLOTS,
              AOE,
              RANGED_POWER,
              RANGED_BOUNCE,
              AMMO
      );
      this.addToolTags(STTItems.power_bank.get(),
              MODIFIABLE,MULTIPART_TOOL,
              SPECIAL_TOOLS,
              BONUS_SLOTS
      );
      this.addToolTags(STTItems.battle_flag.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              INTERACTABLE_RIGHT,
              SWORD,
              SMALL_TOOLS,
              BONUS_SLOTS,
              INTERACTABLE_CHARGE
      );
      this.addToolTags(STTItems.yoyo.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SMALL_TOOLS,
              BONUS_SLOTS,
              AOE
      );
      addArmorTags(STTItems.embeddedArmor,
              MULTIPART_TOOL,
              DURABILITY,
              BONUS_SLOTS,
              DYEABLE,
              TRIM
      );
      this.addToolTags(STTItems.grappling_hook.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SMALL_TOOLS,
              BONUS_SLOTS,
              AOE
      );
      this.addToolTags(STTItems.shuriken.get(),
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SMALL_TOOLS,
              BONUS_SLOTS,
              AOE
      );

      this.optionalToolTags(
              EBItem.first_fractal,
              MODIFIABLE,MULTIPART_TOOL,
              DURABILITY,
              HARVEST,
              MELEE_PRIMARY,
              INTERACTABLE_RIGHT,
              SWORD,
              BROAD_TOOLS,
              BONUS_SLOTS,
              ItemTags.SWORDS,
              AOE
      );
      this.optionalToolTags(
              GoetyItem.tinker_wand,
              MODIFIABLE,MULTIPART_TOOL,
              HARVEST,
              INTERACTABLE_RIGHT,
              BROAD_TOOLS,
              BONUS_SLOTS
      );
      this.optionalToolTags(
              ISSItem.tinker_spell_book,
              MODIFIABLE,MULTIPART_TOOL,
              BROAD_TOOLS,
              BONUS_SLOTS
      );
      this.optionalToolTags(
              WizardryItem.wizardryWand,
              DURABILITY,
              MODIFIABLE,MULTIPART_TOOL,
              HARVEST,
              INTERACTABLE_RIGHT,
              BROAD_TOOLS,
              BONUS_SLOTS
      );



      var curioTag = this.tag(STTTags.Items.CURIO_MODIFIABLE);
      curioTag.addTag(STTTags.Items.TINKER_CHARM);
      curioTag.add(STTItems.tinker_charm.get());
      curioTag.add(STTItems.power_bank.get());
      curioTag.addOptional(SakuraTinker.getResource("tinker_spell_book"));

      this.tag(STTTags.Items.TINKER_CHARM).add(STTItems.tinker_charm.get());
      this.tag(STTTags.Items.TINKER_SPELL_BOOK).addOptional(SakuraTinker.getResource("tinker_spell_book"));
      this.tag(TinkerTags.Items.MODIFIABLE).addTag(STTTags.Items.CURIO_MODIFIABLE);

      this.tag(MODIFIABLE).addTags(STTTags.Items.CURIO_MODIFIABLE);
   }

   private void addPartCastTags(CastItemObject cast, boolean own,
                                TagsProvider.TagAppender<Item> multiUseCasts,
                                TagsProvider.TagAppender<Item> singleUseCasts) {
      String path = cast.getId().getPath();
      String name = path.endsWith("_cast")
              ? path.substring(0, path.length() - "_cast".length()) : path;
      TagKey<Item> multiKey = TagKey.create(Registries.ITEM,
              ResourceLocation.fromNamespaceAndPath("tconstruct", "casts/multi_use/" + name));
      TagKey<Item> singleKey = TagKey.create(Registries.ITEM,
              ResourceLocation.fromNamespaceAndPath("tconstruct", "casts/single_use/" + name));
      var multiTag = this.tag(multiKey);
      var singleTag = this.tag(singleKey);
      if (own) {
         multiTag.add(cast.get());
         singleTag.add(cast.getSand()).add(cast.getRedSand());
      } else {
         multiTag.addOptional(cast.getId());
         singleTag.addOptional(cast.getSand().builtInRegistryHolder().key().location())
                 .addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
      }
      multiUseCasts.addTag(multiKey);
      singleUseCasts.addTag(singleKey);


   }


   private void addNonFlammableTag(WoodBlockObject object) {
      this.tag(ItemTags.NON_FLAMMABLE_WOOD)
              .add(object.asItem(), object.getSlab().asItem(), object.getStairs().asItem(),
                      object.getFence().asItem(), object.getFenceGate().asItem(), object.getDoor().asItem(), object.getTrapdoor().asItem(),
                      object.getPressurePlate().asItem(), object.getButton().asItem())
              .addTag(object.getLogItemTag());
   }

   /**
    * Adds relevant tags for a metal object
    * @param metal  Metal object
    */
   private void addMetalTags(MetalItemObject metal) {
      this.tag(metal.getIngotTag()).add(metal.getIngot());
      this.tag(Tags.Items.INGOTS).addTag(metal.getIngotTag());
      this.tag(metal.getNuggetTag()).add(metal.getNugget());
      this.tag(Tags.Items.NUGGETS).addTag(metal.getNuggetTag());
      this.copy(metal.getBlockTag(), metal.getBlockItemTag());
   }

   @SafeVarargs
   private void addToolTags(ItemLike tool, TagKey<Item>... tags) {
      Item item = tool.asItem();
      for (TagKey<Item> tag : tags) {
         this.tag(tag).add(item);
      }
   }

   @SafeVarargs
   private void optionalToolTags(IdAwareObject tool, TagKey<Item>... tags) {
      ResourceLocation id = tool.getId();
      for (TagKey<Item> tag : tags) {
         this.tag(tag).addOptional(id);
      }
   }

   private TagKey<Item> getArmorTag(ArmorItem.Type slotType) {
      return switch (slotType) {
         case BOOTS -> BOOTS;
         case LEGGINGS -> LEGGINGS;
         case CHESTPLATE -> CHESTPLATES;
         case HELMET -> HELMETS;
      };
   }

   private TagKey<Item> getForgeArmorTag(ArmorItem.Type slotType) {
      return switch (slotType) {
         case BOOTS -> Tags.Items.ARMORS_BOOTS;
         case LEGGINGS -> Tags.Items.ARMORS_LEGGINGS;
         case CHESTPLATE -> Tags.Items.ARMORS_CHESTPLATES;
         case HELMET -> Tags.Items.ARMORS_HELMETS;
      };
   }

   @SafeVarargs
   private void addArmorTags(EnumObject<ArmorItem.Type,? extends Item> armor, TagKey<Item>... tags) {
      armor.forEach((type, item) -> {
         for (TagKey<Item> tag : tags) {
            this.tag(tag).add(item);
         }
         this.tag(getArmorTag(type)).add(item);
         this.tag(getForgeArmorTag(type)).add(item);
      });
   }
}
