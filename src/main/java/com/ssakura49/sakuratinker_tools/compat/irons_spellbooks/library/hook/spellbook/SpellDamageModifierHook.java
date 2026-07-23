package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface SpellDamageModifierHook {
   float getSpellDamage(IToolStackView var1, ModifierEntry var2, SpellAttackContext var3, float var4, float var5);

   public static record AllMerger(Collection<SpellDamageModifierHook> modules) implements SpellDamageModifierHook {
      public float getSpellDamage(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float baseDamage, float damage) {
         for(SpellDamageModifierHook module : this.modules) {
            damage = module.getSpellDamage(tool, modifier, context, baseDamage, damage);
         }

         return damage;
      }
   }
}
