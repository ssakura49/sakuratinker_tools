package com.ssakura49.sakuratinker_tools.library.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public record FoxMaskMaterialStats(int invulnerableTime) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.getResource("fox_mask_part"));
   public static final MaterialStatType<FoxMaskMaterialStats> TYPE;
   private static final String INVUL_TIME;
   private static final List<Component> DESCRIPTION;

   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      info.add(IToolStat.formatColoredBonus(INVUL_TIME, (float)this.invulnerableTime));
      return info;
   }

   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   public void apply(@NotNull ModifierStatsBuilder modifierStatsBuilder, float v) {
      STToolStats.INVULNERABLE_TIME.update(modifierStatsBuilder, (float)this.invulnerableTime * v);
   }

   static {
      TYPE = new MaterialStatType<>(ID, new FoxMaskMaterialStats(0), RecordLoadable.create(IntLoadable.FROM_ZERO.defaultField("invulnerable_time", 0, true, FoxMaskMaterialStats::invulnerableTime), FoxMaskMaterialStats::new));
      INVUL_TIME = IMaterialStats.makeTooltipKey(SakuraTinker.getResource("invulnerable_time"));
      DESCRIPTION = ImmutableList.of(STToolStats.INVULNERABLE_TIME.getDescription());
   }
}
