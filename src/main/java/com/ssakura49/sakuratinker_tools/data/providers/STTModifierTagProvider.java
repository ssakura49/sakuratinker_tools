package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSModifier;
import com.ssakura49.sakuratinker_tools.data.STTTagKeys;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import com.ssakura49.sakuratinker_tools.init.STTModifiers;
import com.ssakura49.sakuratinker_tools.init.STTTags;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags.Modifiers;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

public class STTModifierTagProvider extends AbstractModifierTagProvider {
   public STTModifierTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
      super(packOutput, "sakuratinker", existingFileHelper);
   }

   protected void addTags() {
      this.tag(Modifiers.OVERSLIME_FRIEND);
      this.tag(STTTagKeys.Modifiers.SPECIAL_TOOL)
              .add(new ResourceLocation[]{
                      STTModifiers.ATTACK_MODIFIER.getId(),
                      STTModifiers.BLADE_CONVERGENCE.getId(),
                      STTModifiers.DEFENCE_MODIFIER.getId(),
                      STTModifiers.ENERGY_DISPLAY.getId(),
                      STTModifiers.HEAVY.getId(),
                      STTModifiers.REVOLVER_BULLET.getId(),
                      STTModifiers.SCYTHE_SWEEPING.getId(),
                      STTModifiers.SWIFT_SWORD_TECHNIQUE.getId()}
              ).addOptional(
                      ISSModifier.ArcaneTinkering.getId()
              );
   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tools Modifier Tag Provider.";
   }
}
