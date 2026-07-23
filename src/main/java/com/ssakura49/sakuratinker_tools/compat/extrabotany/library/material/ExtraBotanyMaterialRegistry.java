package com.ssakura49.sakuratinker_tools.compat.extrabotany.library.material;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.stats.PhantomCoreMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

public class ExtraBotanyMaterialRegistry {
    public static final MaterialStatsId FIRST_FRACTAL = new MaterialStatsId(SakuraTinker.getResource("first_fractal"));

    public static void init() {
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        registry.registerStatType(PhantomCoreMaterialStats.TYPE, FIRST_FRACTAL);
    }
}
