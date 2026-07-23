package com.ssakura49.sakuratinker_tools.library.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public record LaserMediumMaterialStats(float range, float cooldown, int penetration) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId("sakuratinker", "laser_medium");
   public static final MaterialStatType<LaserMediumMaterialStats> TYPE;
   private static final String RANGE_PREFIX;
   private static final String COOLDOWN_PREFIX;
   private static final String PENETRATION;
   private static final List<Component> DESCRIPTION;

   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(RANGE_PREFIX, this.range));
      info.add(IToolStat.formatColoredBonus(COOLDOWN_PREFIX, this.cooldown));
      info.add(IToolStat.formatColoredBonus(PENETRATION, (float)this.penetration));
      return info;
   }

   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(ModifierStatsBuilder builder, float scale) {
      STToolStats.RANGE.update(builder, this.range * scale);
      STToolStats.COOLDOWN.update(builder, this.cooldown * scale);
      STToolStats.PENETRATION.update(builder, (float)this.penetration * scale);
   }

   static {
      TYPE = new MaterialStatType<>(ID, new LaserMediumMaterialStats(0.0F, 0.0F, 0), RecordLoadable.create(FloatLoadable.ANY.defaultField("range", 1.0F, true, LaserMediumMaterialStats::range), FloatLoadable.ANY.defaultField("cooldown", 0.0F, true, LaserMediumMaterialStats::cooldown), IntLoadable.FROM_ZERO.defaultField("penetration", 1, true, LaserMediumMaterialStats::penetration), LaserMediumMaterialStats::new));
      RANGE_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("range"));
      COOLDOWN_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("cooldown"));
      PENETRATION = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("penetration"));
      DESCRIPTION = ImmutableList.of(IMaterialStats.makeTooltip(SakuraTinker.getResource("laser_medium.range.description")), IMaterialStats.makeTooltip(SakuraTinker.getResource("laser_medium.cooldown.description")), IMaterialStats.makeTooltip(SakuraTinker.getResource("laser_medium.penetration.description")));
   }
}
