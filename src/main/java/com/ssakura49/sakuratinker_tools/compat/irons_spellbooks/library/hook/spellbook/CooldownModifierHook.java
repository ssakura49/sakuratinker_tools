package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CooldownModifierHook {
   int getCooldown(IToolStackView var1, ModifierEntry var2, int var3, int var4);

   public static record AllMerger(Collection<CooldownModifierHook> modules) implements CooldownModifierHook {
      public int getCooldown(IToolStackView tool, ModifierEntry modifier, int baseCooldown, int currentCooldown) {
         for(CooldownModifierHook module : this.modules) {
            currentCooldown = module.getCooldown(tool, modifier, baseCooldown, currentCooldown);
         }

         return currentCooldown;
      }
   }
}
