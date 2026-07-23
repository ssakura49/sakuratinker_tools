package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import io.redspace.ironsspellbooks.api.events.InscribeSpellEvent;
import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface InscribeSpellModifierHook {
   default void onInscribeSpell(IToolStackView tool, ModifierEntry modifier, InscribeSpellEvent event) {
   }

   public static record AllMerger(Collection<InscribeSpellModifierHook> modules) implements InscribeSpellModifierHook {
      public void onInscribeSpell(IToolStackView tool, ModifierEntry modifier, InscribeSpellEvent event) {
         for(InscribeSpellModifierHook module : this.modules) {
            module.onInscribeSpell(tool, modifier, event);
         }

      }
   }
}
