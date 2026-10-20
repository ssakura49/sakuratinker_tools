package com.ssakura49.sakuratinker_tools.compat.wizardry.init;

import com.binaris.wizardry.setup.registries.Elements;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.element.WandElements;
import com.ssakura49.sakuratinker_tools.compat.wizardry.tool.modifier.ElementAffinityModifier;
import com.ssakura49.sakuratinker_tools.compat.wizardry.tool.modifier.WandManaBarModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import java.util.Map;

public class WizardryModifier {
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create("sakuratinker");

    public static final StaticModifier<WandManaBarModifier> WAND_MANA_BAR = MODIFIERS.register("wand_mana_bar", WandManaBarModifier::new);

    //示例
    public static final StaticModifier<ElementAffinityModifier> FIRE_AFFINITY =
            MODIFIERS.register("fire_affinity", () -> new ElementAffinityModifier(Map.of(Elements.FIRE, 0.05F)));

    public static final StaticModifier<ElementAffinityModifier> ICE_AFFINITY =
            MODIFIERS.register("ice_affinity", () -> new ElementAffinityModifier(Map.of(Elements.ICE, 0.05F)));

    public static final StaticModifier<ElementAffinityModifier> FORGE_AFFINITY =
            MODIFIERS.register("forge_affinity", () -> new ElementAffinityModifier(Map.of(WandElements.FORGE, 0.05F)));
}
