package com.ssakura49.sakuratinker_tools.library.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.chat.Component;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public record FletchingMaterialStats(float velocity, float accuracy) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId("sakuratinker", "fletching");
   public static final MaterialStatType<FletchingMaterialStats> TYPE;
   private static final String VELOCITY_BONUS_PREFIX;
   private static final String ACCURACY_BONUS_PREFIX;
   private static final List<Component> DESCRIPTION;

   public MaterialStatType<?> getType() {
      return TYPE;
   }

   public List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(VELOCITY_BONUS_PREFIX, this.velocity));
      info.add(IToolStat.formatColoredBonus(ACCURACY_BONUS_PREFIX, this.accuracy));
      return info;
   }

   public List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(ModifierStatsBuilder builder, float scale) {
      ToolStats.VELOCITY.add(builder, (double)(this.velocity * scale));
      ToolStats.ACCURACY.add(builder, (double)(this.accuracy * scale));
   }

   public int durability() {
      return 0;
   }

   static {
      TYPE = new MaterialStatType<>(ID, new FletchingMaterialStats(0.0F, 0.0F), RecordLoadable.create(FloatLoadable.ANY.defaultField("velocity", 0.0F, true, FletchingMaterialStats::velocity), FloatLoadable.ANY.defaultField("accuracy", 0.0F, true, FletchingMaterialStats::accuracy), FletchingMaterialStats::new));
      VELOCITY_BONUS_PREFIX = IMaterialStats.makeTooltipKey(TConstruct.getResource("velocity"));
      ACCURACY_BONUS_PREFIX = IMaterialStats.makeTooltipKey(TConstruct.getResource("accuracy"));
      DESCRIPTION = ImmutableList.of(ToolStats.VELOCITY.getDescription());
   }
}
