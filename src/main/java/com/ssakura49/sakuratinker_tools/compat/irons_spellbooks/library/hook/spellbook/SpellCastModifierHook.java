package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import java.util.Collection;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface SpellCastModifierHook {
   default void onPreCast(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, CastSource source, int spellLevel) {
   }

   default void onCast(IToolStackView tool, ModifierEntry entry, SpellAttackContext context, CastSource source) {
   }

   public static record AllMerger(Collection<SpellCastModifierHook> modules) implements SpellCastModifierHook {
      public void onPreCast(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, CastSource source, int spellLevel) {
         for(SpellCastModifierHook module : this.modules) {
            module.onPreCast(tool, modifier, context, source, spellLevel);
         }

      }

      public void onCast(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, CastSource source) {
         for(SpellCastModifierHook module : this.modules) {
            module.onCast(tool, modifier, context, source);
         }

      }
   }
}
