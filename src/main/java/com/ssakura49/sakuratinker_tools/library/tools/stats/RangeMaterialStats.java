package com.ssakura49.sakuratinker_tools.library.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public record RangeMaterialStats(float range) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId("sakuratinker", "range_core");
   public static final MaterialStatType<RangeMaterialStats> TYPE;
   private static final String RANGE_PREFIX;
   private static final List<Component> DESCRIPTION;

   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(RANGE_PREFIX, this.range));
      return info;
   }

   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(ModifierStatsBuilder builder, float scale) {
      STToolStats.RANGE.update(builder, this.range * scale);
   }

   static {
      TYPE = new MaterialStatType<>(ID, new RangeMaterialStats(0.0F), RecordLoadable.create(FloatLoadable.ANY.defaultField("range", 1.0F, true, RangeMaterialStats::range), RangeMaterialStats::new));
      RANGE_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("range"));
      DESCRIPTION = ImmutableList.of(IMaterialStats.makeTooltip(SakuraTinker.getResource("tool.raneg_stats.description")));

   }
}
