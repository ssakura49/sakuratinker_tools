package com.ssakura49.sakuratinker_tools.compat.goety.tool.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import java.util.List;

import com.ssakura49.sakuratinker_tools.compat.goety.library.tool.stat.GoetyToolStats;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public record SoulGathererMaterialStats(float soulPower, float soulIncrease) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "soul_gatherer");
   public static final MaterialStatType<SoulGathererMaterialStats> TYPE = new MaterialStatType<>(ID, new SoulGathererMaterialStats(0, 0), RecordLoadable.create(
           FloatLoadable.ANY.defaultField("soul_power", 0F, SoulGathererMaterialStats::soulPower),
           FloatLoadable.ANY.defaultField("soul_increase", 0F, SoulGathererMaterialStats::soulIncrease), SoulGathererMaterialStats::new
   ));
   private static final String SOUL_POWER = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("soul_power"));
   private static final String SOUL_INCREASE = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("soul_increase"));
   private static final List<Component> DESCRIPTION = ImmutableList.of(
           IMaterialStats.makeTooltip(SakuraTinker.getResource("soul_power")),
           IMaterialStats.makeTooltip(SakuraTinker.getResource("soul_increase"))
   );

    @Override
   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   @Override
   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(SOUL_POWER, this.soulPower));
      info.add(IToolStat.formatColoredPercentBoost(SOUL_INCREASE, this.soulIncrease));
      return info;
   }

   @Override
   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   @Override
   public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
      GoetyToolStats.SOUL_POWER.update(builder, this.soulPower * scale);
      GoetyToolStats.SOUL_INCREASE.update(builder, this.soulIncrease * scale);
   }

   public float soulPower(){
      return this.soulPower;
   }
   public float soulIncrease(){
      return this.soulIncrease;
   }
}
