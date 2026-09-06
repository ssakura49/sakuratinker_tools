package com.ssakura49.sakuratinker_tools.data.enums;

import com.ssakura49.sakuratinker_tools.compat.goety.tool.stats.SoulGathererMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.BookMarkMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.EnvelopeMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.ManuScriptMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.AlchemicalCoreMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.AxleMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.BattleFlagMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.CharmChainMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.ChordMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.EnergyUnitMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.FletchingMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.FoxMaskMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.LaserMediumMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.stats.PhantomCoreMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.RangeMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.STTStatlessMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.YoYoRingMaterialStats;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;

public enum STTEnumTconExtraStats {
   bone(new IMaterialStats[]{
           STTStatlessMaterialStats.CHARM_CORE,
           new CharmChainMaterialStats(0.02F, 8, 0.05F, 0.03F, 0.05F, 0.0F),
           new PhantomCoreMaterialStats(1.0F, 8.0F,1.1f),
           new AxleMaterialStats(100.0F, 30.0F, 3.0F),
           new YoYoRingMaterialStats(1.0F),
           new RangeMaterialStats(5.0F),
           new FletchingMaterialStats(0.2F, 0.2F),
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:blood"), 0.1F))
   }),

   cobalt(new IMaterialStats[]{
           STTStatlessMaterialStats.CHARM_CORE,
           new CharmChainMaterialStats(0.4F, 18, 0.1F, 0.1F, 0.15F, 0.15F),
           new EnergyUnitMaterialStats(50000, 0.3F),
           new LaserMediumMaterialStats(15.0F, 0.6F, 8),
           new ManuScriptMaterialStats(2.5F, 0.15F, 0.15F, 15.0F, 1600.0F),
           new PhantomCoreMaterialStats(2.0F, 16.0F,1.3f),
           new AxleMaterialStats(180.0F, 20.0F, 2.5F),
           new YoYoRingMaterialStats(1.0F),
           new ChordMaterialStats(6.0F),
           new RangeMaterialStats(10.0F),
           new BookMarkMaterialStats(0.1F, 12)
   }),

   copper(new IMaterialStats[]{
           STTStatlessMaterialStats.CHARM_CORE,
           new CharmChainMaterialStats(0.0F, 8, 0.05F, 0.0F, 0.05F, 0.05F),
           new EnergyUnitMaterialStats(15000, 0.15F),
           new LaserMediumMaterialStats(8.0F, 1.5F, 2),
           new PhantomCoreMaterialStats(1.0F, 8.0F,0.8f),
           new AxleMaterialStats(80.0F, 30.0F, 3.5F),
           new YoYoRingMaterialStats(1.0F),
           new RangeMaterialStats(5.0F),
           new FoxMaskMaterialStats(5)
   }),

   manyullyn(new IMaterialStats[]{
           STTStatlessMaterialStats.CHARM_CORE,
           new CharmChainMaterialStats(0.05F, 30, 0.25F, 0.1F, 0.2F, 0.2F),
           new EnergyUnitMaterialStats(80000, 0.6F),
           new LaserMediumMaterialStats(40.0F, 0.4F, 12),
           new PhantomCoreMaterialStats(2.0F, 16.0F,1.6f),
           new AxleMaterialStats(280.0F, 15.0F, 4.5F),
           new YoYoRingMaterialStats(1.0F),
           new ChordMaterialStats(8.0F),
           new AlchemicalCoreMaterialStats(2.0F, 0.4F),
           new RangeMaterialStats(15.0F),
           new ManuScriptMaterialStats(5.0F, 0.3F, 0.2F, 20.0F, 1000.0F),
           new BookMarkMaterialStats(0.15F, 15)
   }),

   wood(new IMaterialStats[]{
           STTStatlessMaterialStats.CHARM_CORE,
           new CharmChainMaterialStats(0.1F, 8, 0.05F, 0.0F, 0.0F, 0.0F),
           new EnergyUnitMaterialStats(5000, 0.1F),
           new LaserMediumMaterialStats(5.0F, 2.0F, 1),
           new BattleFlagMaterialStats(4, 20, 20, 120),
           new AxleMaterialStats(60.0F, 40.0F, 1.5F),
           new YoYoRingMaterialStats(1.0F),
           new SoulGathererMaterialStats(1.0F, 0.0F),
           new ManuScriptMaterialStats(0.0F, 0.05F, 0.0F, 2.0F, 100.0F),
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:nature"), 0.05F)),
           new BookMarkMaterialStats(0.02F, 4)
   }),

   skyslime_vine(new IMaterialStats[]{
           STTStatlessMaterialStats.CHARM_CORE,
           new CharmChainMaterialStats(-0.1F, 8, 0.0F, 0.0F, 0.05F, 0.05F),
           new ChordMaterialStats(8.0F)
   }),

   nahualt(new IMaterialStats[]{
           STTStatlessMaterialStats.CHARM_CORE,
           new CharmChainMaterialStats(0.05F, 14, 0.05F, 0.05F, 0.1F, 0.1F),
           new PhantomCoreMaterialStats(2.0F, 16.0F,1.25f)
   }),

   iron(new IMaterialStats[]{
           new PhantomCoreMaterialStats(1.0F, 8.0F, 1.1F),
           new FoxMaskMaterialStats(8),
           new BookMarkMaterialStats(0.01F, 8)
   }),

   string(new IMaterialStats[]{
           new ChordMaterialStats(8.0F)
   }),

   bamboo(new IMaterialStats[]{
           new ManuScriptMaterialStats(1.0F, 0.15F, -0.1F, 3.0F, 1000.0F)
   }),

   obsidian(new IMaterialStats[]{
           new ManuScriptMaterialStats(2.0F, -0.1F, -0.1F, 5.0F, 500.0F)
   }),

   paper(new IMaterialStats[]{
           new ManuScriptMaterialStats(0.0F, 0.0F, 0.15F, 0.0F, 120.0F)
   }),

   gold(new IMaterialStats[]{
           new FoxMaskMaterialStats(10)
   }),

   blazingBone(new IMaterialStats[]{
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:fire"), 0.15F))
   }),

   blazewood(new IMaterialStats[]{
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:fire"), 0.1F)),
           new ManuScriptMaterialStats(5.0F, 0.05F, 0.0F, 8.0F, 150.0F)
   }),

   chorus(new IMaterialStats[]{
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:ender"), 0.1F)),
           new ManuScriptMaterialStats(8.0F, 0.05F, 0.02F, 10.0F, 280.0F)
   }),

   ice(new IMaterialStats[]{
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:ice"), 0.05F))
   }),

   witherBone(new IMaterialStats[]{
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:blood"), 0.1F))
   }),

   venomBone(new IMaterialStats[]{
           new EnvelopeMaterialStats(Map.of(ResourceLocation.parse("irons_spellbooks:nature"), 0.1F))
   });

   public final IMaterialStats[] stats;

   STTEnumTconExtraStats(IMaterialStats... stats) {
      this.stats = stats;
   }
}
