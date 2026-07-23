package com.ssakura49.sakuratinker_tools.data.providers;

import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;

public class STTMaterialProvider extends AbstractMaterialDataProvider {
   public STTMaterialProvider(PackOutput packOutput) {
      super(packOutput);
   }

   protected void addMaterials() {
   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tools Material Data Provider";
   }
}
