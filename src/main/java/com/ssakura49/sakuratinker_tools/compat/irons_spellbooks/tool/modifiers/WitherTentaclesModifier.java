package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers;

import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.ISSHooks;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook.SpellHitModifierHook;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class WitherTentaclesModifier extends BaseModifier implements SpellHitModifierHook {
   protected void registerHooks(ModuleHookMap.@NotNull Builder builder) {
      super.registerHooks(builder);
      builder.addHook(this, ISSHooks.SPELL_HIT);
   }

   public boolean isNoLevels() {
      return true;
   }

   public void afterSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damageDealt) {
      LivingEntity target = context.getLivingTarget();
      if (context.getSchoolType() == SchoolRegistry.BLOOD.get() && target != null) {
         target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0, true, true, true));
      }

   }
}
