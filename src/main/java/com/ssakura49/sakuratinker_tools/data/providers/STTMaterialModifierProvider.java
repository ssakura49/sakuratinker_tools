package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker_tools.data.enums.STTEnumTconMaterialModifier;
import com.ssakura49.sakuratinker_tools.data.enums.STTEnumTconMaterials;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;

public class STTMaterialModifierProvider extends AbstractMaterialTraitDataProvider {
   public STTMaterialModifierProvider(PackOutput packOutput) {
      super(packOutput, new STTMaterialProvider(packOutput));
   }

   protected void addMaterialTraits() {
      for(STTEnumTconMaterials material : STTEnumTconMaterials.values()) {
         for(STTEnumTconMaterialModifier modifier : material.modifiers) {
            this.addTraits(material.id, modifier.statType, modifier.modifiers);
         }
      }

   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tools Material Modifier Provider";
   }
}
