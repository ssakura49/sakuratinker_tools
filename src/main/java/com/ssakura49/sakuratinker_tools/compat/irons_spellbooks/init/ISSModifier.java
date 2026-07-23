package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers.ArcaneConstructModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers.CobaltSpellModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers.GreedyBookModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers.SpellFrozenModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers.ToxicityIntensificationModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers.WitherTentaclesModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class ISSModifier {
   public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create("sakuratinker");
   public static StaticModifier<ArcaneConstructModifier> ArcaneTinkering;
   public static StaticModifier<CobaltSpellModifier> cobaltSpell;
   public static StaticModifier<SpellFrozenModifier> SpellFrozen;
   public static StaticModifier<WitherTentaclesModifier> WitherTentacles;
   public static StaticModifier<GreedyBookModifier> GreedyBook;
   public static StaticModifier<ToxicityIntensificationModifier> ToxicityIntensification;

   static {
      ArcaneTinkering = MODIFIERS.register("arcane_tinkering", ArcaneConstructModifier::new);
      cobaltSpell = MODIFIERS.register("cobalt_spell", CobaltSpellModifier::new);
      SpellFrozen = MODIFIERS.register("spell_frozen", SpellFrozenModifier::new);
      WitherTentacles = MODIFIERS.register("wither_tentacles", WitherTentaclesModifier::new);
      GreedyBook = MODIFIERS.register("greedy_book", GreedyBookModifier::new);
      ToxicityIntensification = MODIFIERS.register("toxicity_intensification", ToxicityIntensificationModifier::new);

   }
}
