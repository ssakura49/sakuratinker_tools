package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.stats.PhantomCoreMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.stats.SoulGathererMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.BookMarkMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.EnvelopeMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.ISSStatlessMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.ManuScriptMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;
import slimeknights.tconstruct.tools.stats.GripMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class STTPartSpriteProvider extends AbstractPartSpriteProvider {
   public STTPartSpriteProvider() {
      super("sakuratinker");
   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tool Part Sprite Provider";
   }

   protected void addAllSpites() {
      this.addSprite("part/charm_chain/charm_chain", CharmChainMaterialStats.ID);
      this.addSprite("part/charm_core/charm_core", STTStatlessMaterialStats.CHARM_CORE.getIdentifier());
      this.addSprite("part/charm_setting/charm_setting",STTStatlessMaterialStats.CHARM_SETTING.getIdentifier());
      this.addSprite("part/swift_blade/swift_blade", HeadMaterialStats.ID);
      this.addSprite("part/swift_guard/swift_guard", HandleMaterialStats.ID);
      this.addSprite("part/great_blade/great_blade", HeadMaterialStats.ID);
      this.addSprite("part/blade_box/blade_box", StatlessMaterialStats.BINDING.getIdentifier());
      this.addSprite("part/barrel/barrel", HeadMaterialStats.ID);
      this.addSprite("part/energy_unit/energy_unit", EnergyUnitMaterialStats.ID);
      this.addSprite("part/laser_medium/laser_medium", LaserMediumMaterialStats.ID);
      this.addSprite("part/blade/blade", HeadMaterialStats.ID);
      this.addSprite("part/arrow_head/arrow_head", HeadMaterialStats.ID);
      this.addSprite("part/arrow_shaft/arrow_shaft", HandleMaterialStats.ID);
      this.addSprite("part/fletching/fletching", FletchingMaterialStats.ID);
      this.addSprite("part/envelope/envelope", EnvelopeMaterialStats.ID);
      this.addSprite("part/book_mark/book_mark", BookMarkMaterialStats.ID);
      this.addSprite("part/manu_script/manu_script", ManuScriptMaterialStats.ID);
      this.addSprite("part/gutter/gutter", ISSStatlessMaterialStats.GUTTER.getIdentifier());
      this.addSprite("part/shell/shell", StatlessMaterialStats.BINDING.getIdentifier());
      this.addSprite("part/flag/flag", BattleFlagMaterialStats.ID);
      this.addSprite("part/axle/axle", AxleMaterialStats.ID);
      this.addSprite("part/chord/chord", ChordMaterialStats.ID);
      this.addSprite("part/yoyo_ring/yoyo_ring", YoYoRingMaterialStats.ID);
      this.addSprite("part/yoyo_body/yoyo_body", HeadMaterialStats.ID);
      this.addSprite("part/scythe_blade/scythe_blade", HeadMaterialStats.ID);
      this.addSprite("part/soul_gatherer/soul_gatherer", SoulGathererMaterialStats.ID);
      this.addSprite("part/phantom_core/phantom_core", PhantomCoreMaterialStats.ID);
      this.addSprite("part/revolver_chamber/revolver_chamber", RevolverChamberMaterialStats.ID);

      this.buildTool("tinker_charm")
              .addPart("charm_chain", CharmChainMaterialStats.ID)
              .addPart("charm_setting", STTStatlessMaterialStats.CHARM_SETTING.getIdentifier())
              .addPart("charm_core", STTStatlessMaterialStats.CHARM_CORE.getIdentifier());
      this.buildTool("swift_sword")
              .addBreakableHead("blade")
              .addBinding("binding")
              .addBinding("guard")
              .addHandle("handle");
      this.buildTool("great_sword")
              .addBreakableHead("head")
              .addHead("plate_1")
              .addHead("plate_2")
              .addBinding("tough_binding")
              .addHandle("tough_handle");
      this.buildTool("blade_convergence")
              .addBreakableHead("blade")
              .addHead("plate")
              .addHandle("handle");
      this.buildTool("vampire_knife")
              .addHead("blade1")
              .addHead("blade2")
              .addHead("blade3")
              .addBinding("blade_box");
      this.buildTool("laser_gun")
              .addBreakableHead("barrel")
              .addPart("energy_unit", EnergyUnitMaterialStats.ID)
              .addPart("laser_medium", LaserMediumMaterialStats.ID)
              .addHandle("tough_handle");
      this.buildTool("shuriken")
              .addHead("blade_1")
              .addHead("blade_2")
              .addHead("blade_3")
              .addHead("blade_4");
      this.buildTool("tinker_arrow")
              .addHead("arrow_head")
              .addHandle("arrow_shaft")
              .addPart("fletching", FletchingMaterialStats.ID);
      this.buildTool("tinker_spell_book")
              .addPart("envelope", EnvelopeMaterialStats.ID)
              .addPart("book_mark", BookMarkMaterialStats.ID)
              .addPart("manu_script", ManuScriptMaterialStats.ID)
              .addPart("gutter", ISSStatlessMaterialStats.GUTTER.getIdentifier());
      this.buildTool("grappling_hook")
              .addBreakableHead("blade")
              .addPart("bowstring", StatlessMaterialStats.BOWSTRING.getIdentifier())
              .addPart("limb1", LimbMaterialStats.ID)
              .addPart("limb2", LimbMaterialStats.ID)
              .addHandle("handle");
      this.buildTool("power_bank")
              .addBinding("shell")
              .addPart("battery_1", EnergyUnitMaterialStats.ID)
              .addPart("battery_2", EnergyUnitMaterialStats.ID)
              .addPart("battery_3", EnergyUnitMaterialStats.ID);
      this.buildTool("battle_flag")
              .addBreakableHead("head")
              .addPart("flag", BattleFlagMaterialStats.ID)
              .addPart("flag_broken", BattleFlagMaterialStats.ID)
              .addHandle("handle_1")
              .addHandle("handle_2");
      this.buildTool("first_fractal")
              .addBreakableHead("head")
              .addBinding("binding")
              .addHandle("handle");
      this.buildTool("yoyo")
              .addHead("body_1")
              .addHead("body_2")
              .addPart("axle", AxleMaterialStats.ID)
              .addPart("chord", ChordMaterialStats.ID)
              .addPart("ring", YoYoRingMaterialStats.ID);
      this.buildTool("scythe")
              .addBreakableHead("scythe_blade");
      this.buildTool("tinker_wand")
              .addPart("soul_gatherer", SoulGathererMaterialStats.ID)
              .addHead("large_plate")
              .addHandle("tough_handle")
              .addBinding("tough_binding");
      this.buildTool("tinker_bullet")
              .addHead("bullet_1")
              .addHead("bullet_2");
      this.buildTool("revolver")
              .addPart("grip", GripMaterialStats.ID)
              .addPart("limb", LimbMaterialStats.ID)
              .addPart("chamber", RevolverChamberMaterialStats.ID)
              .addHandle("handle");
      this.buildTool("first_fractal")
              .addBreakableHead("head")
              .addPart("core", PhantomCoreMaterialStats.ID)
              .addBinding("binding")
              .addHandle("handle");

      this.addTexture("tinker_armor/embedded/upper_armor", TinkerPartSpriteProvider.ARMOR_PLATING).disallowAnimated();
      this.addTexture("tinker_armor/embedded/upper_leggings", TinkerPartSpriteProvider.ARMOR_PLATING).disallowAnimated();
      this.addTexture("tinker_armor/embedded/middle_armor", TinkerPartSpriteProvider.ARMOR_PLATING).disallowAnimated();
      this.addTexture("tinker_armor/embedded/middle_leggings", TinkerPartSpriteProvider.ARMOR_PLATING).disallowAnimated();
      this.addTexture("tinker_armor/embedded/under_armor", TinkerPartSpriteProvider.ARMOR_MAILLE).disallowAnimated();
      this.addTexture("tinker_armor/embedded/under_leggings", TinkerPartSpriteProvider.ARMOR_MAILLE).disallowAnimated();
      this.addTexture("tinker_armor/embedded/maille_wings", TinkerPartSpriteProvider.ARMOR_MAILLE).disallowAnimated();

      for(ArmorItem.Type slot : Type.values()) {
         String slotName = slot.getName();
         MaterialStatsId platingId = (PlatingMaterialStats.TYPES.get(slot.ordinal())).getId();
         String baseName = slot == Type.HELMET ? "head" : slotName;
         this.buildTool("armor/embedded/" + slotName).disallowAnimated().addBreakablePart(baseName + "1", platingId).addBreakablePart(baseName + "2", platingId).addBreakablePart(baseName + "3", StatlessMaterialStats.MAILLE.getIdentifier());
      }

   }
}
