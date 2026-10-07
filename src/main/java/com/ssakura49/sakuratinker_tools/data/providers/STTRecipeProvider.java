package com.ssakura49.sakuratinker_tools.data.providers;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import com.ssakura49.sakuratinker_tools.util.SafeClass;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.casting.material.CompositeCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.PartSwapCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.partbuilder.PartRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;

public class STTRecipeProvider extends RecipeProvider implements ISmelteryRecipeHelper, IToolRecipeHelper {
   public STTRecipeProvider(PackOutput output) {
      super(output);
   }

   @Override
   protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
      String partsFolder = "parts/";
      String castsFolder = "casts/";
      String toolsFolder = "tools/build/";
      String armorFolder = "tools/armor/";

      registerPart(consumer, STTItems.charm_chain,   STTItems.charmChainCast,   4, partsFolder, castsFolder);
      registerPart(consumer, STTItems.charm_core,    STTItems.charmCoreCast,    2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.swift_blade,   STTItems.swiftBladeCast,   3, partsFolder, castsFolder);
      registerPart(consumer, STTItems.swift_guard,   STTItems.swiftGuardCast,   2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.laser_medium,  STTItems.laserMediumCast,  4, partsFolder, castsFolder);
      registerPart(consumer, STTItems.energy_unit,   STTItems.energyUnitCast,   4, partsFolder, castsFolder);
      registerPart(consumer, STTItems.barrel,        STTItems.barrelCast,       2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.blade,         STTItems.bladeCast,        2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.arrow_head,    STTItems.arrowHeadCast,    2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.arrow_shaft,   STTItems.arrowShaftCast,   2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.great_blade,   STTItems.greatBladeCast,   6, partsFolder, castsFolder);
      registerPart(consumer, STTItems.shell,         STTItems.shellCast,        2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.flag,          STTItems.flagCast,         4, partsFolder, castsFolder);
      registerPart(consumer, STTItems.axle,          STTItems.axleCast,         2, partsFolder, castsFolder);
      registerPart(consumer, STTItems.yoyo_ring,     STTItems.yoyoRingCast,     1, partsFolder, castsFolder);
      registerPart(consumer, STTItems.chord,         STTItems.chordCast,        1, partsFolder, castsFolder);
      registerPart(consumer, STTItems.yoyo_body,     STTItems.yoyoBodyCast,     3, partsFolder, castsFolder);
      registerPart(consumer, STTItems.scythe_blade,  STTItems.scytheBladeCast,  5, partsFolder, castsFolder);
      registerPart(consumer, STTItems.range_core,    STTItems.rangeCoreCast,    4, partsFolder, castsFolder);
      registerPart(consumer,STTItems.charm_setting,STTItems.charmSettingCast,4,partsFolder,castsFolder);
      //registerPart(consumer, STTItems.phantom_core,  STTItems.phantomCoreCast,  3, partsFolder, castsFolder);

      uncastablePart(consumer, STTItems.fletching.get(), 2, null, partsFolder);
      //registerPart(consumer, STTItems.blade_box, STTItems., 1, null, partsFolder);
      //registerPart(consumer, STTItems.fox_mask_part, 2, null, partsFolder);


      toolBuilding(consumer, STTItems.tinker_charm,      toolsFolder);
      toolBuilding(consumer, STTItems.swift_sword,       toolsFolder);
      toolBuilding(consumer, STTItems.laser_gun,         toolsFolder);
      toolBuilding(consumer, STTItems.tinker_arrow,      toolsFolder);
      toolBuilding(consumer, STTItems.vampire_knife,     toolsFolder);
      toolBuilding(consumer, STTItems.great_sword,       toolsFolder);
      toolBuilding(consumer, STTItems.power_bank,        toolsFolder);
      toolBuilding(consumer, STTItems.battle_flag,       toolsFolder);
      toolBuilding(consumer, STTItems.yoyo,              toolsFolder);
      toolBuilding(consumer, STTItems.scythe,            toolsFolder);
      toolBuilding(consumer, STTItems.blade_convergence, toolsFolder);
      toolBuilding(consumer, STTItems.grappling_hook,    toolsFolder);
      toolBuilding(consumer, STTItems.revolver,          toolsFolder);
      toolBuilding(consumer, STTItems.tinker_bullet,     toolsFolder);
      toolBuilding(consumer, STTItems.shuriken,          toolsFolder);

      String embeddedFolder = armorFolder + "plate/";
      STTItems.embeddedArmor.forEach(item -> toolBuilding(consumer, item, embeddedFolder, new Pattern(SakuraTinker.getResource("embedded_armor"))));
      PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(STTItems.embeddedArmor.get(ArmorItem.Type.HELMET)), 3)
              .save(consumer, location(embeddedFolder + "helmet_swapping"));
      PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(STTItems.embeddedArmor.get(ArmorItem.Type.CHESTPLATE)), 6)
              .save(consumer, location(embeddedFolder + "chestplate_swapping"));
      PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(STTItems.embeddedArmor.get(ArmorItem.Type.LEGGINGS)), 5)
              .save(consumer, location(embeddedFolder + "leggings_swapping"));
      PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(STTItems.embeddedArmor.get(ArmorItem.Type.BOOTS)), 2)
              .save(consumer, location(embeddedFolder + "boots_swapping"));

//      Consumer<FinishedRecipe> extrabotanyConditional = withCondition(consumer, modLoaded("extrabotany"));
//      MaterialCastingRecipeBuilder.tableRecipe()
//              .setCast(ExtraBotanyItems.heroMedal, true)
//              .setItemCost(6)
//              .save(extrabotanyConditional, ResourceLocation.fromNamespaceAndPath(getModId(), "tools/casting/phantom_core_from_hero_medal"));
//
      Consumer<FinishedRecipe> goety = withCondition(consumer, modLoaded(SafeClass.GOETY));
      registerPart(goety,GoetyItem.soul_gatherer,GoetyItem.soulGathererCast,8,partsFolder,castsFolder);
      toolBuilding(goety, GoetyItem.tinker_wand,toolsFolder);


      Consumer<FinishedRecipe> iss = withCondition(consumer, modLoaded(SafeClass.ISS));
      registerPart(iss,ISSItem.envelope, ISSItem.envelopeCast,4,partsFolder,castsFolder);
      registerPart(iss,ISSItem.book_mark,ISSItem.bookMarkCast,1,partsFolder,castsFolder);
      registerPart(iss,ISSItem.manu_script,ISSItem.manuScriptCast,6,partsFolder,castsFolder);
      registerPart(iss,ISSItem.gutter,ISSItem.gutterCast,3,partsFolder,castsFolder);
      toolBuilding(iss, ISSItem.tinker_spell_book,toolsFolder);

      Consumer<FinishedRecipe> extraBotany = withCondition(consumer, modLoaded(SafeClass.ExtraBotany));
      MaterialCastingRecipeBuilder.tableRecipe(EBItem.first_fractal.get())
              .setCast(ExtraBotanyItems.heroMedal, true)
              .setItemCost(6)
              .save(extraBotany, ResourceLocation.fromNamespaceAndPath(getModId(), "tools/casting/phantom_core_from_hero_medal"));
      toolBuilding(extraBotany, EBItem.first_fractal,toolsFolder);
   }


   private void registerPart(Consumer<FinishedRecipe> consumer, Supplier<? extends IMaterialItem> part, CastItemObject cast, int cost, String partFolder, String castFolder) {
      this.registerPart(consumer, (IMaterialItem) part.get(), cast, cost, partFolder, castFolder);
   }

   private void registerPart(Consumer<FinishedRecipe> consumer, IMaterialItem part, CastItemObject cast, int cost, String partFolder, String castFolder) {
      ResourceLocation partId = id(part);
      String name = partId.getPath();
      TagKey<Item> singleTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tconstruct", "casts/single_use/" + name));
      TagKey<Item> multiTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tconstruct", "casts/multi_use/" + name));
      PartRecipeBuilder.partRecipe(part)
              .setPattern(partId)
              .setPatternItem(CompoundIngredient.of(
                      Ingredient.of(TinkerTags.Items.DEFAULT_PATTERNS),
                      Ingredient.of(cast.get())))
              .setCost(cost)
              .save(consumer, location(partFolder + "builder/" + name));
      String castingFolder = partFolder + "casting/";
      MaterialCastingRecipeBuilder.tableRecipe(part)
              .setItemCost(cost)
              .setCast(multiTag, false)
              .save(consumer, location(castingFolder + name + "_gold_cast"));
      MaterialCastingRecipeBuilder.tableRecipe(part)
              .setItemCost(cost)
              .setCast(singleTag, true)
              .save(consumer, location(castingFolder + name + "_sand_cast"));
      CompositeCastingRecipeBuilder.table(part, cost)
              .save(consumer, location(castingFolder + name + "_composite"));
      this.castCreation(consumer, MaterialIngredient.of(part), cast, castFolder, name);
   }

   private ResourceLocation getResource(String path) {
      return ResourceLocation.fromNamespaceAndPath(getModId(), path);
   }
   private ResourceLocation preFix(ResourceLocation id, String prefix) {
      return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), prefix + id.getPath());
   }
   private ResourceLocation ids(ItemLike item) {
      return ForgeRegistries.ITEMS.getKey(item.asItem());
   }

   public static ICondition modLoaded(String modId) {
      return new ModLoadedCondition(modId);
   }

   public static ICondition tagFilled(TagKey<Item> tagKey) {
      return new TagFilledCondition<>(tagKey);
   }

   public @NotNull String getModId() {
      return "sakuratinker";
   }
}
