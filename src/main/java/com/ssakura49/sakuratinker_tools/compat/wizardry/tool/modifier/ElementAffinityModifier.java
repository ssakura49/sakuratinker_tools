package com.ssakura49.sakuratinker_tools.compat.wizardry.tool.modifier;

import com.binaris.wizardry.api.content.spell.Element;
import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.modifier.EbWizardryModifierHooks;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.modifier.hook.ElementAffinityModifierHook;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Map;

public class ElementAffinityModifier extends BaseModifier implements ElementAffinityModifierHook {
    private final Map<Element, Float> affinities;

    public ElementAffinityModifier(Map<Element, Float> affinities) {
        this.affinities = Map.copyOf(affinities);
    }

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, EbWizardryModifierHooks.MODIFY_ELEMENT);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public Map<Element, Float> getAffinities(IToolStackView tool, ModifierEntry entry) {
        return affinities;
    }
}
