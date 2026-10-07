package com.ssakura49.sakuratinker_tools.library.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public record RevolverChamberMaterialStats(float recoil, float spread) implements IMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId("sakuratinker", "revolver_chamber");
    public static final MaterialStatType<RevolverChamberMaterialStats> TYPE = new MaterialStatType<>(ID,
            new RevolverChamberMaterialStats(0.0F, 0.0F),
            RecordLoadable.create(
                    FloatLoadable.ANY.defaultField("recoil", 0.0F, true, RevolverChamberMaterialStats::recoil),
                    FloatLoadable.ANY.defaultField("spread", 0.0F, true, RevolverChamberMaterialStats::spread),
                    RevolverChamberMaterialStats::new));
    private static final String RECOIL_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("recoil"));
    private static final String SPREAD_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("spread"));
    private static final List<Component> DESCRIPTION = ImmutableList.of(
            IMaterialStats.makeTooltip(SakuraTinker.getResource("tool.stats.recoil.description")),
            IMaterialStats.makeTooltip(SakuraTinker.getResource("tool.stats.spread.description"))
    );


    public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
        STToolStats.RECOIL.update(builder, this.recoil * scale);
        STToolStats.SPREAD.update(builder, this.spread * scale);
    }

    public @NotNull MaterialStatType<?> getType() {
        return TYPE;
    }

    public @NotNull List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(IToolStat.formatColoredBonus(RECOIL_PREFIX, this.recoil));
        info.add(IToolStat.formatColoredBonus(SPREAD_PREFIX, this.spread));
        return info;
    }

    public @NotNull List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

}
