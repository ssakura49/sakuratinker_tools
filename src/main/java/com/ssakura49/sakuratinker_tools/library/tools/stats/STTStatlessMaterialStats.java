package com.ssakura49.sakuratinker_tools.library.tools.stats;

import com.ssakura49.sakuratinker.SakuraTinker;
import java.util.List;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public enum STTStatlessMaterialStats implements IMaterialStats {
   CHARM_CORE("charm_core"),
   SHELL("shell"),
   GUTTER("gutter");

   private static final List<Component> LOCALIZED = List.of(IMaterialStats.makeTooltip(TConstruct.getResource("extra.no_stats")));
   private static final List<Component> DESCRIPTION = List.of(Component.empty());
   private final MaterialStatType<STTStatlessMaterialStats> type;

   private STTStatlessMaterialStats(String name) {
      this.type = MaterialStatType.singleton(new MaterialStatsId(SakuraTinker.getResource(name)), this);
   }

   public MaterialStatType<STTStatlessMaterialStats> getType() {
      return this.type;
   }

   public List<Component> getLocalizedInfo() {
      return LOCALIZED;
   }

   public List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(ModifierStatsBuilder builder, float scale) {
   }

   // $FF: synthetic method
   private static STTStatlessMaterialStats[] $values() {
      return new STTStatlessMaterialStats[]{CHARM_CORE, SHELL, GUTTER};
   }
}
