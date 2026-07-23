package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface SpellHitModifierHook {
   default float beforeSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damage, float baseKnockback, float knockback) {
      return knockback;
   }

   default void afterSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damageDealt) {
   }

   default void failedSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damageAttempted) {
   }

   public static record AllMerger(Collection<SpellHitModifierHook> modules) implements SpellHitModifierHook {
      public float beforeSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damage, float baseKnockback, float knockback) {
         for(SpellHitModifierHook module : this.modules) {
            knockback = module.beforeSpellHit(tool, modifier, context, damage, baseKnockback, knockback);
         }

         return knockback;
      }

      public void afterSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damageDealt) {
         for(SpellHitModifierHook module : this.modules) {
            module.afterSpellHit(tool, modifier, context, damageDealt);
         }

      }

      public void failedSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damageAttempted) {
         for(SpellHitModifierHook module : this.modules) {
            module.failedSpellHit(tool, modifier, context, damageAttempted);
         }

      }
   }
}
