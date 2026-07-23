package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker_tools.data.enums.STTEnumTconMaterials;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;

public class STTMaterialStatProvider extends AbstractMaterialStatsDataProvider {
   public STTMaterialStatProvider(PackOutput packOutput) {
      super(packOutput, new STTMaterialProvider(packOutput));
   }

   protected void addMaterialStats() {
      for(STTEnumTconMaterials material : STTEnumTconMaterials.values()) {
         this.addMaterialStats(material.id, material.stats.stats);
      }

   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tools Material Stats Data Provider";
   }
}
