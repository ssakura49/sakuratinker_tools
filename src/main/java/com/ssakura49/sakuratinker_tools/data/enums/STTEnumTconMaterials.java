package com.ssakura49.sakuratinker_tools.data.enums;

import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public enum STTEnumTconMaterials {
   bone(STTEnumTconExtraStats.bone, MaterialIds.bone, new STTEnumTconMaterialModifier[0]),
   cobalt(STTEnumTconExtraStats.cobalt, MaterialIds.cobalt, new STTEnumTconMaterialModifier[]{STTEnumTconMaterialModifier.cobalt_m}),
   copper(STTEnumTconExtraStats.copper, MaterialIds.copper, new STTEnumTconMaterialModifier[0]),
   manyullyn(STTEnumTconExtraStats.manyullyn, MaterialIds.manyullyn, new STTEnumTconMaterialModifier[0]),
   wood(STTEnumTconExtraStats.wood, MaterialIds.wood, new STTEnumTconMaterialModifier[0]),
   skyslime_vine(STTEnumTconExtraStats.skyslime_vine, MaterialIds.skyslimeVine, new STTEnumTconMaterialModifier[0]),
   nahualt(STTEnumTconExtraStats.nahualt, MaterialIds.nahuatl, new STTEnumTconMaterialModifier[0]),
   iron(STTEnumTconExtraStats.iron, MaterialIds.iron, new STTEnumTconMaterialModifier[0]),
   string(STTEnumTconExtraStats.string, MaterialIds.string, new STTEnumTconMaterialModifier[0]),
   bamboo(STTEnumTconExtraStats.bamboo, MaterialIds.bamboo, new STTEnumTconMaterialModifier[0]),
   obsidian(STTEnumTconExtraStats.obsidian, MaterialIds.obsidian, new STTEnumTconMaterialModifier[0]),
   gold(STTEnumTconExtraStats.gold, MaterialIds.gold, new STTEnumTconMaterialModifier[0]),
   blazingBone(STTEnumTconExtraStats.blazingBone, MaterialIds.blazingBone, new STTEnumTconMaterialModifier[0]),
   blazewood(STTEnumTconExtraStats.blazewood, MaterialIds.blazewood, new STTEnumTconMaterialModifier[0]),
   ice(STTEnumTconExtraStats.ice, MaterialIds.ice, new STTEnumTconMaterialModifier[]{STTEnumTconMaterialModifier.ice_m}),
   witherBone(STTEnumTconExtraStats.witherBone, MaterialIds.necroticBone, new STTEnumTconMaterialModifier[]{STTEnumTconMaterialModifier.witherBone_m}),
   venomBone(STTEnumTconExtraStats.venomBone, MaterialIds.venombone, new STTEnumTconMaterialModifier[]{STTEnumTconMaterialModifier.venomBone_m});

   public final STTEnumTconExtraStats stats;
   public final STTEnumTconMaterialModifier[] modifiers;
   public final MaterialId id;

   STTEnumTconMaterials(STTEnumTconExtraStats stats, MaterialId id, STTEnumTconMaterialModifier... modifiers) {
      this.stats = stats;
      this.modifiers = modifiers;
      this.id = id;
   }
}
