package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.tool.stat.ISSToolStats;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;

import java.util.List;
import java.util.Map;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.mapping.MapLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.utils.Util;

public record EnvelopeMaterialStats(Map<ResourceLocation, Float> schoolIds) implements IMaterialStats {
   public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "envelope");
   private static final MapLoadable<ResourceLocation, Float> RL_MAP_LOADER = new MapLoadable<>(
           Loadables.RESOURCE_LOCATION,
           FloatLoadable.ANY,
           0
   );
   public static final MaterialStatType<EnvelopeMaterialStats> TYPE =
           new MaterialStatType<>(ID,
                   new EnvelopeMaterialStats(Map.of()),
                   RecordLoadable.create(
                           RL_MAP_LOADER.defaultField(
                                   "school_bonus",
                                   Map.of(),
                                   EnvelopeMaterialStats::schoolIds
                           ),
                           EnvelopeMaterialStats::new
                   )
           );
   private static final String SCHOOL_BONUS =
           IMaterialStats.makeTooltipKey(SakuraTinker.getResource("school_bonus"));
   private static final List<Component> DESCRIPTION = ImmutableList.of(
           IMaterialStats.makeTooltip(SakuraTinker.getResource("school_bonus"))
   );

   @Override
   public @NotNull MaterialStatType<?> getType() {
      return TYPE;
   }

   @Override
   public @NotNull List<Component> getLocalizedInfo() {
      List<Component> info = Lists.newArrayList();
      for (Map.Entry<ResourceLocation, Float> entry : schoolIds.entrySet()) {
         float bonus = entry.getValue();
         SchoolType school = SchoolRegistry.getSchool(entry.getKey());
         MutableComponent schoolName = school != null
                 ? school.getDisplayName().copy()
                 : Component.literal(entry.getKey().toString());
         float hue = Mth.positiveModulo(0.5F + bonus, 2.0F);
         TextColor color = TextColor.fromRgb(Mth.hsvToRgb(hue / 1.5F, 1.0F, 0.75F));
         MutableComponent formattedValue = Component.literal(Util.BONUS_FORMAT.format(bonus))
                 .withStyle(style -> style.withColor(color));
         info.add(schoolName.append(Component.literal(": ")).append(formattedValue));

      }
      return info;
   }

   @Override
   public @NotNull List<Component> getLocalizedDescriptions() {
      return DESCRIPTION;
   }

   @Override
   public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
      for (Map.Entry<ResourceLocation, Float> entry : schoolIds.entrySet()) {
         SchoolType school = SchoolRegistry.getSchool(entry.getKey());
         if (school == null) {
            continue;
         }
         FloatToolStat stat = ISSToolStats.getSchoolStat(school);
         if (stat == null) {
            continue;
         }
         stat.update(builder, entry.getValue() * scale);
      }
   }
}