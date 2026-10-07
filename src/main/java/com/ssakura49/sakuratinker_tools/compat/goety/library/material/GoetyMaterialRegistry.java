package com.ssakura49.sakuratinker_tools.compat.goety.library.material;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.stats.SoulGathererMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

@Deprecated
public class GoetyMaterialRegistry {
    public static final MaterialStatsId TINKER_DARK_WAND = new MaterialStatsId(SakuraTinker.getResource("tinker_dark_wand"));

    public static void init() {
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        registry.registerStatType(SoulGathererMaterialStats.TYPE, TINKER_DARK_WAND);
    }
}
