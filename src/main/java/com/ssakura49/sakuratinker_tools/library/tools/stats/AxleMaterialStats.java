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

public record AxleMaterialStats(float time, float attackInterval, float weight) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId("sakuratinker", "axle");
   public static final MaterialStatType<AxleMaterialStats> TYPE;
   private static final String TIME;
   private static final String ATTACK_INTERVAL;
   private static final String WEIGHT;
   private static final List<Component> DESCRIPTION;

   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(TIME, this.time));
      info.add(IToolStat.formatColoredBonus(ATTACK_INTERVAL, this.attackInterval));
      info.add(IToolStat.formatColoredBonus(WEIGHT, this.weight));
      return info;
   }

   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
      STToolStats.TIME.update(builder, this.time * scale);
      STToolStats.ATTACK_INTERVAL.update(builder, this.attackInterval * scale);
      STToolStats.WEIGHT.update(builder, this.weight * scale);
   }

   static {
      TYPE = new MaterialStatType<>(ID, new AxleMaterialStats(0.0F, 0.0F, 0.0F), RecordLoadable.create(FloatLoadable.ANY.defaultField("time", 0.0F, AxleMaterialStats::time), FloatLoadable.ANY.defaultField("attack_interval", 0.0F, AxleMaterialStats::attackInterval), FloatLoadable.ANY.defaultField("weight", 0.0F, AxleMaterialStats::weight), AxleMaterialStats::new));
      TIME = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("time"));
      ATTACK_INTERVAL = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("attack_interval"));
      WEIGHT = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("weight"));
      DESCRIPTION = ImmutableList.of(IMaterialStats.makeTooltip(SakuraTinker.getResource("time")), IMaterialStats.makeTooltip(SakuraTinker.getResource("attack_interval")), IMaterialStats.makeTooltip(SakuraTinker.getResource("weight")));
   }
}
