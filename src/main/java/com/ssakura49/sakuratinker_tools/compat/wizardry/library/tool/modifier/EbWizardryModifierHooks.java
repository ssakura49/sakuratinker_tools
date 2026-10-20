package com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.modifier;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.modifier.hook.ElementAffinityModifierHook;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.data.registry.IdAwareComponentRegistry;
import slimeknights.tconstruct.library.module.ModuleHook;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class EbWizardryModifierHooks {
    public static final IdAwareComponentRegistry<ModuleHook<?>> LOADER = new IdAwareComponentRegistry<>("Modifier Hook");
    public static <T> ModuleHook<T> register(ResourceLocation name, Class<T> filter, @Nullable Function<Collection<T>, T> merger, T defaultInstance) {
        return LOADER.register(new ModuleHook<>(name, filter, merger, defaultInstance));
    }
    public static final ModuleHook<ElementAffinityModifierHook> MODIFY_ELEMENT = register(
            SakuraTinker.getResource("element_affinity"),
            ElementAffinityModifierHook.class,
            ElementAffinityModifierHook.AllMerger::new,
            (tool, entry) -> Map.of()
    );
}
