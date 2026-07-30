package com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import java.util.List;

import com.ssakura49.sakuratinker_tools.compat.extrabotany.library.tool.stat.EBToolStats;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public record PhantomCoreMaterialStats(float phantom_amount, float range, float move_speed) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "phantom_core");
   public static final MaterialStatType<PhantomCoreMaterialStats> TYPE = new MaterialStatType<>(ID, new PhantomCoreMaterialStats(0.0f, 0.0f,0.0f), RecordLoadable.create(
           FloatLoadable.ANY.defaultField("phantom_amount", 1.0f, true, PhantomCoreMaterialStats::phantom_amount),
           FloatLoadable.ANY.defaultField("range", 1.0f, true, PhantomCoreMaterialStats::range),
           FloatLoadable.ANY.defaultField("move_speed",1.0f,true,PhantomCoreMaterialStats::move_speed),
           PhantomCoreMaterialStats::new
   ));
   private static final String AMOUNT_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("phantom_amount"));
   private static final String RANGE_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("range"));
   private static final String MOVE_SPEED_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("move_speed"));

   private static final List<Component> DESCRIPTION = ImmutableList.of(
           IMaterialStats.makeTooltip(SakuraTinker.getResource("first_fractal.phantom_amount.description")),
           IMaterialStats.makeTooltip(SakuraTinker.getResource("first_fractal.range.description")),
           IMaterialStats.makeTooltip(SakuraTinker.getResource("first_fractal.move_speed.description"))
   );

   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(AMOUNT_PREFIX, this.phantom_amount));
      info.add(IToolStat.formatColoredBonus(RANGE_PREFIX, this.range));
      info.add(IToolStat.formatColoredPercentBoost(MOVE_SPEED_PREFIX, this.move_speed));
      return info;
   }

   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
      EBToolStats.PHANTOM_AMOUNT.update(builder, this.phantom_amount * scale);
      STToolStats.RANGE.update(builder, this.range * scale);
      STToolStats.MOVE_SPEED.update(builder, this.move_speed * scale);
   }

   public float phantom_amount() {
      return this.phantom_amount;
   }
   public float range(){
      return this.range;
   }
   public float move_speed() {
      return move_speed;
   }
}
