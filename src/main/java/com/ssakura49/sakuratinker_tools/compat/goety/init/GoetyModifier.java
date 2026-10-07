package com.ssakura49.sakuratinker_tools.compat.goety.init;

import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class GoetyModifier {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create("sakuratinker");
    public static StaticModifier<NoLevelsModifier> G_SOUL_INFUSION = MODIFIERS.register("g_soul_infusion", NoLevelsModifier::new);
}
