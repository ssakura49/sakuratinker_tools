package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.stats.SoulGathererMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.*;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.stats.PhantomCoreMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

public class STTMaterialRegistry {
   public static final MaterialStatsId CHARM = new MaterialStatsId(SakuraTinker.getResource("charm"));
   public static final MaterialStatsId LASER_GUN = new MaterialStatsId(SakuraTinker.getResource("laser_gun"));
   public static final MaterialStatsId TINKER_ARROW = new MaterialStatsId(SakuraTinker.getResource("tinker_arrow"));
   public static final MaterialStatsId POWER_BANK = new MaterialStatsId(SakuraTinker.getResource("power_bank"));
   public static final MaterialStatsId BATTLE_FLAG = new MaterialStatsId(SakuraTinker.getResource("battle_flag"));
   public static final MaterialStatsId FOX_MASK = new MaterialStatsId(SakuraTinker.getResource("fox_mask"));
   public static final MaterialStatsId YO_YO = new MaterialStatsId(SakuraTinker.getResource("yo_yo"));
   public static final MaterialStatsId ALCHEMICAL_GLOVES = new MaterialStatsId(SakuraTinker.getResource("alchemical_gloves"));
   public static final MaterialStatsId RANGE = new MaterialStatsId(SakuraTinker.getResource("range"));
   public static final MaterialStatsId REVOLVER = new MaterialStatsId(SakuraTinker.getResource("revolver"));

   public static void init() {
      IMaterialRegistry registry = MaterialRegistry.getInstance();
      registry.registerStatType(CharmChainMaterialStats.TYPE, CHARM);
      registry.registerStatType(STTStatlessMaterialStats.CHARM_CORE.getType(), CHARM);
      registry.registerStatType(STTStatlessMaterialStats.CHARM_SETTING.getType(), CHARM);
      registry.registerStatType(LaserMediumMaterialStats.TYPE, LASER_GUN);
      registry.registerStatType(EnergyUnitMaterialStats.TYPE, LASER_GUN);
      registry.registerStatType(FletchingMaterialStats.TYPE, TINKER_ARROW);
      registry.registerStatType(STTStatlessMaterialStats.SHELL.getType(), POWER_BANK);
      registry.registerStatType(BattleFlagMaterialStats.TYPE, BATTLE_FLAG);
      registry.registerStatType(YoYoRingMaterialStats.TYPE, YO_YO);
      registry.registerStatType(AxleMaterialStats.TYPE, YO_YO);
      registry.registerStatType(ChordMaterialStats.TYPE, YO_YO);
      registry.registerStatType(AlchemicalCoreMaterialStats.TYPE, ALCHEMICAL_GLOVES);
      registry.registerStatType(RangeMaterialStats.TYPE, RANGE);
      registry.registerStatType(FoxMaskMaterialStats.TYPE, FOX_MASK);
      registry.registerStatType(RevolverChamberMaterialStats.TYPE, REVOLVER);
      registry.registerStatType(PhantomCoreMaterialStats.TYPE, new MaterialStatsId(SakuraTinker.getResource("first_fractal")));
      registry.registerStatType(SoulGathererMaterialStats.TYPE, new MaterialStatsId(SakuraTinker.getResource("tinker_dark_wand")));
   }
}
