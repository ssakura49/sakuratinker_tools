package com.ssakura49.sakuratinker_tools.library.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public record EnergyUnitMaterialStats(int energyStorage, float durability) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId("sakuratinker", "energy_unit");
   public static final MaterialStatType<EnergyUnitMaterialStats> TYPE;
   private static final String ENERGY_STORAGE_PREFIX;
   private static final String DURABILITY_PREFIX;
   private static final List<Component> DESCRIPTION;

   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(ENERGY_STORAGE_PREFIX, (float)this.energyStorage));
      info.add(IToolStat.formatColoredPercentBoost(DURABILITY_PREFIX, this.durability));
      return info;
   }

   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
      ToolEnergyCapability.MAX_STAT.add(builder, (double)this.energyStorage);
      ToolStats.DURABILITY.percent(builder, (double)this.durability);
   }

   static {
      TYPE = new MaterialStatType<>(ID, new EnergyUnitMaterialStats(0, 0.0F), RecordLoadable.create(IntLoadable.FROM_ZERO.defaultField("max_energy", 0, true, EnergyUnitMaterialStats::energyStorage), FloatLoadable.ANY.defaultField("durability", 1.0F, true, EnergyUnitMaterialStats::durability), EnergyUnitMaterialStats::new));
      ENERGY_STORAGE_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("max_energy"));
      DURABILITY_PREFIX = IMaterialStats.makeTooltipKey(TConstruct.getResource("durability"));
      DESCRIPTION = ImmutableList.of(IMaterialStats.makeTooltip(SakuraTinker.getResource("max_energy.description")), IMaterialStats.makeTooltip(TConstruct.getResource("handle.durability.description")));
   }
}
