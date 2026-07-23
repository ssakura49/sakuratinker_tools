package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.module;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.ISSHooks;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook.CooldownModifierHook;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public record CooldownModule(float percentage, int flat) implements ModifierModule, CooldownModifierHook {
   public static final RecordLoadable<CooldownModule> LOADABLE;

   public int getCooldown(IToolStackView tool, ModifierEntry modifier, int baseCooldown, int currentCooldown) {
      int level = modifier.getLevel();
      float factor = 1.0F - this.percentage * (float)level;
      return (int)((float)currentCooldown * factor) - this.flat * level;
   }

   public @NotNull RecordLoadable<? extends ModifierModule> getLoader() {
      return LOADABLE;
   }

   public @NotNull List<ModuleHook<?>> getDefaultHooks() {
      return List.of(ISSHooks.COOLDOWN);
   }

   static {
      LOADABLE = RecordLoadable.create(FloatLoadable.PERCENT.defaultField("percentage", 0.0F, CooldownModule::percentage), IntLoadable.FROM_ZERO.defaultField("flat", 0, CooldownModule::flat), CooldownModule::new);
   }
}
