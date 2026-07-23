package com.ssakura49.sakuratinker_tools.data.providers;

import java.util.function.Consumer;

import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import com.ssakura49.sakuratinker_tools.util.SafeClass;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipeBuilder;

public class STTRecipeProvider extends RecipeProvider implements ISmelteryRecipeHelper, IToolRecipeHelper {
   public STTRecipeProvider(PackOutput output) {
      super(output);
   }

   @Override
   protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
      String partsFolder = "parts/";
      String castsFolder = "casts/";
      String toolsFolder = "tools/build/";

      partRecipes(consumer, STTItems.charm_chain,   STTItems.charmChainCast,   6, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.charm_core,    STTItems.charmCoreCast,    2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.swift_blade,   STTItems.swiftBladeCast,   3, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.swift_guard,   STTItems.swiftGuardCast,   2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.laser_medium,  STTItems.laserMediumCast,  4, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.energy_unit,   STTItems.energyUnitCast,   4, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.barrel,        STTItems.barrelCast,       2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.blade,         STTItems.bladeCast,        2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.arrow_head,    STTItems.arrowHeadCast,    2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.arrow_shaft,   STTItems.arrowShaftCast,   2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.great_blade,   STTItems.greatBladeCast,   6, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.shell,         STTItems.shellCast,        2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.flag,          STTItems.flagCast,         4, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.axle,          STTItems.axleCast,         2, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.yoyo_ring,     STTItems.yoyoRingCast,     1, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.chord,         STTItems.chordCast,        1, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.yoyo_body,     STTItems.yoyoBodyCast,     3, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.scythe_blade,  STTItems.scytheBladeCast,  5, partsFolder, castsFolder);
      partRecipes(consumer, STTItems.range_core,    STTItems.rangeCoreCast,    4, partsFolder, castsFolder);

      //partRecipes(consumer, STTItems.phantom_core,  STTItems.phantomCoreCast,  3, partsFolder, castsFolder);

      uncastablePart(consumer, STTItems.fletching.get(), 2, null, partsFolder);
      //partRecipes(consumer, STTItems.blade_box, STTItems., 1, null, partsFolder);
      //partRecipes(consumer, STTItems.fox_mask_part, 2, null, partsFolder);


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

      //toolBuilding(consumer, STTItems.first_fractal,     toolsFolder);

//      Consumer<FinishedRecipe> extrabotanyConditional = withCondition(consumer, modLoaded("extrabotany"));
//      MaterialCastingRecipeBuilder.tableRecipe()
//              .setCast(ExtraBotanyItems.heroMedal, true)
//              .setItemCost(6)
//              .save(extrabotanyConditional, ResourceLocation.fromNamespaceAndPath(getModId(), "tools/casting/phantom_core_from_hero_medal"));
//
      Consumer<FinishedRecipe> goety = withCondition(consumer, modLoaded(SafeClass.GOETY));
      partRecipes(goety,GoetyItem.soul_gatherer,GoetyItem.soulGathererCast,8,partsFolder,castsFolder);
      toolBuilding(goety, GoetyItem.tinker_wand,toolsFolder);


      Consumer<FinishedRecipe> iss = withCondition(consumer, modLoaded(SafeClass.ISS));
      partRecipes(iss,ISSItem.envelope, ISSItem.envelopeCast,4,partsFolder,castsFolder);
      partRecipes(iss,ISSItem.book_mark,ISSItem.bookMarkCast,1,partsFolder,castsFolder);
      partRecipes(iss,ISSItem.manu_script,ISSItem.manuScriptCast,6,partsFolder,castsFolder);
      partRecipes(iss,ISSItem.gutter,ISSItem.gutterCast,3,partsFolder,castsFolder);
      toolBuilding(iss, ISSItem.tinker_spell_book,toolsFolder);

      Consumer<FinishedRecipe> extraBotany = withCondition(consumer, modLoaded(SafeClass.ExtraBotany));
      MaterialCastingRecipeBuilder.tableRecipe(EBItem.first_fractal.get())
              .setCast(ExtraBotanyItems.heroMedal, true)
              .setItemCost(6)
              .save(extraBotany, ResourceLocation.fromNamespaceAndPath(getModId(), "tools/casting/phantom_core_from_hero_medal"));
      toolBuilding(extraBotany, EBItem.first_fractal,toolsFolder);
   }

   private ResourceLocation getResource(String path) {
      return ResourceLocation.fromNamespaceAndPath(getModId(), path);
   }
   private ResourceLocation preFix(ResourceLocation id, String prefix) {
      return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), prefix + id.getPath());
   }
   private ResourceLocation ids(ItemLike item) {
      return BuiltInRegistries.ITEM.getKey(item.asItem());
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
