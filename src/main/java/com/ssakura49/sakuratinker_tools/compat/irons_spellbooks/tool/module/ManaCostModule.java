package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.module;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.ISSHooks;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook.ManaCostModifierHook;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public record ManaCostModule(float percentage, int flat) implements ModifierModule, ManaCostModifierHook {
   public static final RecordLoadable<ManaCostModule> LOADABLE;

   public int getManaCost(IToolStackView tool, ModifierEntry modifier, int baseCost, int currentCost) {
      int level = modifier.getLevel();
      float factor = 1.0F - this.percentage * (float)level;
      return (int)((float)currentCost * factor) - this.flat * level;
   }

   public @NotNull List<ModuleHook<?>> getDefaultHooks() {
      return List.of(ISSHooks.MANA_COST);
   }

   public RecordLoadable<? extends ModifierModule> getLoader() {
      return LOADABLE;
   }

   static {
      LOADABLE = RecordLoadable.create(FloatLoadable.PERCENT.defaultField("percentage", 0.0F, ManaCostModule::percentage), IntLoadable.FROM_ZERO.defaultField("flat", 0, ManaCostModule::flat), ManaCostModule::new);
   }
}
