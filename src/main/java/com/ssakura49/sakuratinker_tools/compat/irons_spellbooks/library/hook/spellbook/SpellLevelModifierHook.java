package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface SpellLevelModifierHook {
   int getSpellLevel(IToolStackView var1, ModifierEntry var2, SpellAttackContext var3, int var4, int var5);

   public static record AllMerger(Collection<SpellLevelModifierHook> modules) implements SpellLevelModifierHook {
      public int getSpellLevel(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, int baseLevel, int currentLevel) {
         for(SpellLevelModifierHook module : this.modules) {
            currentLevel = module.getSpellLevel(tool, modifier, context, baseLevel, currentLevel);
         }

         return currentLevel;
      }
   }
}
