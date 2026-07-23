package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface SpellHealModifierHook {
   default void getSpellHeal(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, SpellHealEvent event) {
   }

   public static record AllMerger(Collection<SpellHealModifierHook> modules) implements SpellHealModifierHook {
      public void getSpellHeal(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, SpellHealEvent event) {
         for(SpellHealModifierHook module : this.modules) {
            module.getSpellHeal(tool, modifier, context, event);
         }

      }
   }
}
