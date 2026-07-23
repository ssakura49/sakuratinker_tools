package com.ssakura49.sakuratinker_tools.data.enums;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.material.ISSMaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public enum STTEnumTconMaterialModifier {
   cobalt_m(ISSMaterialRegistry.TINKER_SPELL_BOOK, new ModifierEntry[]{entry(ISSModifier.cobaltSpell.getId())}),
   ice_m(ISSMaterialRegistry.TINKER_SPELL_BOOK, new ModifierEntry[]{entry(ISSModifier.SpellFrozen.getId())}),
   witherBone_m(ISSMaterialRegistry.TINKER_SPELL_BOOK, new ModifierEntry[]{entry(ISSModifier.WitherTentacles.getId())}),
   manyullyn_m(ISSMaterialRegistry.TINKER_SPELL_BOOK, new ModifierEntry[]{entry(ISSModifier.GreedyBook.getId())}),
   venomBone_m(ISSMaterialRegistry.TINKER_SPELL_BOOK, new ModifierEntry[]{entry(ISSModifier.ToxicityIntensification.getId())});

   public final ModifierEntry[] modifiers;
   public final MaterialStatsId statType;

   STTEnumTconMaterialModifier(MaterialStatsId statType, ModifierEntry... modifiers) {
      this.modifiers = modifiers;
      this.statType = statType;
   }

   public static ModifierEntry entry(ModifierId id, int level) {
      return new ModifierEntry(id, level);
   }

   public static ModifierEntry entry(ModifierId id) {
      return new ModifierEntry(id, 1);
   }
}
