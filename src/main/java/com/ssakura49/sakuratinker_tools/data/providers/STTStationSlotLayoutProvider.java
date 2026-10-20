package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.compat.wizardry.init.WizardryItem;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import com.ssakura49.sakuratinker_tools.library.tools.layout.STPatterns;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.layout.Patterns;
import slimeknights.tconstruct.library.tools.layout.StationSlotLayout;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.function.Supplier;

public class STTStationSlotLayoutProvider extends AbstractStationSlotLayoutProvider implements IConditionBuilder {
   public STTStationSlotLayoutProvider(PackOutput packOutput) {
      super(packOutput);
   }

   protected void addLayouts() {
      this.defineModifiable(STTItems.battle_flag)
              .sortIndex(10)
              .translationKey("item.sakuratinker.battle_flag")
              .addInputItem(TinkerToolParts.smallBlade, 60, 20)
              .addInputItem(STTItems.flag.get(), 40, 20)
              .addInputItem(STTItems.flag.get(), 60, 40)
              .addInputItem(TinkerToolParts.toughHandle, 40, 40)
              .addInputItem(TinkerToolParts.toughHandle, 20, 60)
              .build();
      this.defineModifiable(STTItems.blade_convergence)
              .sortIndex(3)
              .addInputItem(TinkerToolParts.smallBlade, 48, 26)
              .addInputItem(TinkerToolParts.largePlate, 12, 62)
              .addInputItem(STTItems.range_core.get(), 48, 44)
              .addInputItem(TinkerToolParts.toolHandle, 30, 44)
              .build();
      this.definePattern(STPatterns.EMBEDDED_ARMOR)
              .sortIndex(15).translationKey("item.sakuratinker.embedded_armor")
              .addInputPattern(Patterns.PLATING, 30, 30, Ingredient.of((ItemLike[])TinkerToolParts.plating.values().toArray(new Item[0])))
              .addInputItem(TinkerToolParts.maille, 40, 50)
              .addInputPattern(Patterns.PLATING, 50, 30, Ingredient.of((ItemLike[])TinkerToolParts.plating.values().toArray(new Item[0])))
              .build();
      this.defineModifiable(STTItems.grappling_hook)
              .sortIndex(4)
              .addInputItem(TinkerToolParts.smallBlade, 18, 19)
              .addInputItem(TinkerToolParts.bowLimb, 58, 19)
              .addInputItem(TinkerToolParts.bowLimb, 18, 54)
              .addInputItem(TinkerToolParts.bowstring, 38, 36)
              .addInputItem(TinkerToolParts.toughHandle, 58, 54)
              .build();
      this.defineModifiable(STTItems.great_sword)
              .sortIndex(8)
              .addInputItem(STTItems.great_blade, 45, 26)
              .addInputItem(TinkerToolParts.largePlate, 25, 46)
              .addInputItem(TinkerToolParts.largePlate, 25, 26)
              .addInputItem(TinkerToolParts.toughBinding, 45, 46)
              .addInputItem(TinkerToolParts.toughHandle, 7, 62)
              .build();
      this.defineModifiable(STTItems.laser_gun)
              .sortIndex(10)
              .addInputItem(STTItems.blade.get(), 14, 24)
              .addInputItem(STTItems.energy_unit.get(), 53, 55)
              .addInputItem(STTItems.laser_medium.get(), 33, 44)
              .addInputItem(TinkerToolParts.toughHandle, 33, 64)
              .build();
      this.defineModifiable(STTItems.power_bank)
              .sortIndex(10)
              .addInputItem(STTItems.shell.get(), 45, 40)
              .addInputItem(STTItems.energy_unit.get(), 25, 60)
              .addInputItem(STTItems.energy_unit.get(), 25, 40)
              .addInputItem(STTItems.energy_unit.get(), 25, 20)
              .build();
      this.defineModifiable(STTItems.revolver)
              .sortIndex(8)
              .addInputItem(TinkerToolParts.bowLimb, 20, 20)
              .addInputItem(TinkerToolParts.bowGrip, 40, 40)
              .addInputItem(STTItems.revolverChamber,60, 40)
              .addInputItem(TinkerToolParts.toolHandle, 60, 60)
              .build();
      this.defineModifiable(STTItems.scythe)
              .sortIndex(8)
              .addInputItem(STTItems.scythe_blade.get(), 45, 26)
              .addInputItem(TinkerToolParts.largePlate, 25, 26)
              .addInputItem(TinkerToolParts.toughBinding, 45, 46)
              .addInputItem(TinkerToolParts.toughHandle, 7, 62)
              .build();
      this.defineModifiable(STTItems.shuriken)
              .icon(new Pattern(SakuraTinker.MODID,"shuriken"))
              .sortIndex(10)
              .addInputItem(STTItems.blade.get(), 45, 30)
              .addInputItem(STTItems.blade.get(), 25, 50)
              .addInputItem(STTItems.blade.get(), 25, 30)
              .addInputItem(STTItems.blade.get(), 45, 50)
              .build();
      this.defineModifiable(STTItems.swift_sword)
              .sortIndex(4)
              .addInputItem(STTItems.swift_blade.get(), 53, 22)
              .addInputItem(STTItems.swift_guard.get(), 33, 44)
              .addInputItem(TinkerToolParts.toolBinding, 33, 64)
              .addInputItem(TinkerToolParts.toolHandle, 10, 64)
              .build();
      this.defineModifiable(STTItems.tinker_arrow)
              .sortIndex(10)
              .addInputItem(STTItems.arrow_head.get(), 48, 26)
              .addInputItem(STTItems.arrow_shaft.get(), 30, 44)
              .addInputItem(STTItems.fletching.get(), 12, 62)
              .build();
      this.defineModifiable(STTItems.tinker_bullet)
              .sortIndex(3)
              .addInputItem(TinkerToolParts.smallBlade, 20, 30)
              .addInputItem(TinkerToolParts.smallBlade, 40, 30)
              .build();
      this.defineModifiable(STTItems.tinker_charm)
              .icon(new Pattern(SakuraTinker.MODID, "tinker_charm"))
              .sortIndex(20)
              .addInputItem(STTItems.charm_core.get(), 35, 55)
              .addInputItem(STTItems.charm_setting.get(), 15, 55)
              .addInputItem(STTItems.charm_chain.get(), 25, 35)
              .build();
      this.defineModifiable(ISSItem.tinker_spell_book, modLoaded("irons_spellbooks"))
              .sortIndex(20)
              .addInputItem(ISSItem.manu_script.get(), 30, 50)
              .addInputItem(ISSItem.envelope.get(), 30, 30)
              .addInputItem(ISSItem.book_mark.get(), 50, 30)
              .addInputItem(ISSItem.gutter.get(), 50, 50)
              .build();
      this.defineModifiable(STTItems.vampire_knife)
              .sortIndex(4)
              .addInputItem(TinkerToolParts.smallBlade, 28, 32)
              .addInputItem(TinkerToolParts.smallBlade, 48, 32)
              .addInputItem(TinkerToolParts.smallBlade, 48, 52)
              .addInputItem(STTItems.blade_box.get(), 28, 52)
              .build();
      this.defineModifiable(STTItems.yoyo)
              .sortIndex(4)
              .addInputItem(STTItems.yoyo_body.get(), 20, 20)
              .addInputItem(STTItems.yoyo_body.get(), 20, 60)
              .addInputItem(STTItems.axle.get(), 20, 40)
              .addInputItem(STTItems.chord.get(), 40, 40)
              .addInputItem(STTItems.yoyo_ring.get(), 60, 40)
              .build();
      this.defineModifiable(GoetyItem.tinker_wand, modLoaded("goety"))
              .sortIndex(20)
              .addInputItem(GoetyItem.soul_gatherer,52,22)
              .addInputItem(TinkerToolParts.largePlate,32,22)
              .addInputItem(TinkerToolParts.toughHandle,32,42)
              .addInputItem(TinkerToolParts.toughBinding, 12,62)
              .build();
      this.defineModifiable(EBItem.first_fractal, modLoaded("extrabotany"))
              .sortIndex(8)
              .addInputItem(TinkerToolParts.broadBlade,45,25)
              .addInputItem(EBItem.phantom_core,25,25)
              .addInputItem(TinkerToolParts.toughBinding,25,45)
              .addInputItem(TinkerToolParts.toughHandle, 5,65)
              .build();

      this.defineModifiable(WizardryItem.wizardryWand, modLoaded("ebwizardry"))
              .sortIndex(15)
              .addInputItem(WizardryItem.manaCrystal,55,20)
              .addInputItem(TinkerToolParts.toolHandle,35,40)
              .addInputItem(TinkerToolParts.toolBinding, 15,60)
              .build();
   }

   protected StationSlotLayout.Builder defineModifiable(Supplier<? extends IModifiableDisplay> item, ICondition condition) {
      IModifiableDisplay display = item.get();
      return define(display.asItem().builtInRegistryHolder().key().location(), condition)
              .translationKey(display.asItem().getDescriptionId())
              .icon(display.getRenderTool());
   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tools Station Layout Provider";
   }
}
