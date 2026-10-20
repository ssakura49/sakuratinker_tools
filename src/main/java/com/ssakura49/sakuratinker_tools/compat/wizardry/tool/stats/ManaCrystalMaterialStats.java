package com.ssakura49.sakuratinker_tools.compat.wizardry.tool.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.stats.WizardryToolStats;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public record ManaCrystalMaterialStats(int durability, float manaCapacity) implements IRepairableMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "mana_crystal");
    public static final MaterialStatType<ManaCrystalMaterialStats> TYPE = new MaterialStatType<>(ID,
            new ManaCrystalMaterialStats(1,1),
            RecordLoadable.create(
                    IRepairableMaterialStats.DURABILITY_FIELD,
                    FloatLoadable.ANY.defaultField("mana_capacity", 0F, ManaCrystalMaterialStats::manaCapacity),
                    ManaCrystalMaterialStats::new));

    private static final String MANA = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("mana_capacity"));
    private static final List<Component> DESCRIPTION = ImmutableList.of(
            IMaterialStats.makeTooltip(SakuraTinker.getResource("mana_capacity")));

    @Override
    public @NotNull MaterialStatType<?> getType() {
        return TYPE;
    }

    @Override
    public @NotNull List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(ToolStats.DURABILITY.formatValue(this.durability));
        info.add(WizardryToolStats.MANA_CAPACITY.formatValue(this.manaCapacity));
        return info;
    }


    @Override
    public @NotNull List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.update(builder, durability * scale);
        WizardryToolStats.MANA_CAPACITY.update(builder, this.manaCapacity * scale);
    }
}
