package com.ssakura49.sakuratinker_tools.compat.extrabotany.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.modifier.FirstFractalModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class EBModifier {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);
    public static StaticModifier<FirstFractalModifier> FIRST_FRACTAL = MODIFIERS.register("first_fractal", FirstFractalModifier::new);
}
