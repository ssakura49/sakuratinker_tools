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

public record ChordMaterialStats(float length) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId("sakuratinker", "chord");
   public static final MaterialStatType<ChordMaterialStats> TYPE;
   private static final String LENGTH;
   private static final List<Component> DESCRIPTION;

   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(LENGTH, this.length));
      return info;
   }

   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
      STToolStats.LENGTH.update(builder, this.length * scale);
   }

   static {
      TYPE = new MaterialStatType<>(ID, new ChordMaterialStats(0.0F), RecordLoadable.create(FloatLoadable.ANY.defaultField("length", 0.0F, ChordMaterialStats::length), ChordMaterialStats::new));
      LENGTH = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("length"));
      DESCRIPTION = ImmutableList.of(IMaterialStats.makeTooltip(SakuraTinker.getResource("length")));
   }
}
