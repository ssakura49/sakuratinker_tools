package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface SpellSchoolModifierHook {
   SchoolType modifySchool(IToolStackView var1, ModifierEntry var2, AbstractSpell var3, SchoolType var4);

   public static record AllMerger(Collection<SpellSchoolModifierHook> modules) implements SpellSchoolModifierHook {
      public SchoolType modifySchool(IToolStackView tool, ModifierEntry modifier, AbstractSpell spell, SchoolType currentSchool) {
         for(SpellSchoolModifierHook module : this.modules) {
            currentSchool = module.modifySchool(tool, modifier, spell, currentSchool);
         }

         return currentSchool;
      }
   }
}
