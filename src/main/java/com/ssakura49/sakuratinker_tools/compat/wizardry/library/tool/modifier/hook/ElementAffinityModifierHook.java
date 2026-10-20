package com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.modifier.hook;

import com.binaris.wizardry.api.content.spell.Element;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.modifier.EbWizardryModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public interface ElementAffinityModifierHook {
    Map<Element, Float> getAffinities(IToolStackView tool, ModifierEntry entry);

    static float get(IToolStackView tool, Element element) {
        float total = 0.0F;
        for (ModifierEntry entry : tool.getModifierList()) {
            Float value = entry.getHook(EbWizardryModifierHooks.MODIFY_ELEMENT).getAffinities(tool, entry).get(element);
            if (value != null) {
                total += value;
            }
        }
        return total;
    }

    static Map<Element, Float> getAll(IToolStackView tool) {
        Map<Element, Float> result = new HashMap<>();
        for (ModifierEntry entry : tool.getModifierList()) {
            entry.getHook(EbWizardryModifierHooks.MODIFY_ELEMENT).getAffinities(tool, entry)
                    .forEach((element, value) -> result.merge(element, value, Float::sum));
        }
        return result;
    }

    record AllMerger(Collection<ElementAffinityModifierHook> modules) implements ElementAffinityModifierHook {
        @Override
        public Map<Element, Float> getAffinities(IToolStackView tool, ModifierEntry entry) {
            Map<Element, Float> result = new HashMap<>();
            for (ElementAffinityModifierHook module : modules) {
                module.getAffinities(tool, entry)
                        .forEach((element, value) -> result.merge(element, value, Float::sum));
            }
            return result;
        }
    }
}
