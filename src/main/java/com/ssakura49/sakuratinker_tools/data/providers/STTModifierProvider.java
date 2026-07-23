package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker_tools.data.enums.STTEnumModifier;
import java.util.Arrays;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.json.JsonRedirect;

public class STTModifierProvider extends AbstractModifierProvider {
   public STTModifierProvider(PackOutput packOutput) {
      super(packOutput);
   }

   protected void addModifiers() {
      Arrays.stream(STTEnumModifier.values()).toList().forEach((enumModifier) -> this.buildModifier(enumModifier.id, enumModifier.condition, new JsonRedirect[0]).addModules(enumModifier.modules).tooltipDisplay(enumModifier.tooltipDisplay));
   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tools Modifier Provider";
   }
}
