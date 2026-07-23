package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface ManaCostModifierHook {
   int getManaCost(IToolStackView var1, ModifierEntry var2, int var3, int var4);

   public static record AllMerger(Collection<ManaCostModifierHook> modules) implements ManaCostModifierHook {
      public int getManaCost(IToolStackView tool, ModifierEntry modifier, int baseCost, int currentCost) {
         for(ManaCostModifierHook module : this.modules) {
            currentCost = module.getManaCost(tool, modifier, baseCost, currentCost);
         }

         return currentCost;
      }
   }
}
