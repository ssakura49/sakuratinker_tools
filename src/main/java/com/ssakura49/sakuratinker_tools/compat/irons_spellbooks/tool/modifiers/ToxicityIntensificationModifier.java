package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers;

import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.ISSHooks;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook.SpellHitModifierHook;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ToxicityIntensificationModifier extends BaseModifier implements SpellHitModifierHook {
   protected void registerHooks(ModuleHookMap.Builder builder) {
      super.registerHooks(builder);
      builder.addHook(this, ISSHooks.SPELL_HIT);
   }

   public void afterSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damageDealt) {
      LivingEntity target = context.getLivingTarget();
      if (context.getSchoolType() == SchoolRegistry.ICE.get() && target != null && target.hasEffect(MobEffects.POISON)) {
         MobEffectInstance currentPoison = target.getEffect(MobEffects.POISON);
         if (currentPoison != null) {
            int currentDuration = currentPoison.getDuration();
            int currentAmplifier = currentPoison.getAmplifier();
            int newAmplifier = Math.min(9, (currentAmplifier + 1) * 2 - 1);
            int newDuration = currentDuration * 2;
            MobEffectInstance newPoison = new MobEffectInstance(MobEffects.POISON, newDuration, newAmplifier, currentPoison.isAmbient(), currentPoison.isVisible(), currentPoison.showIcon());
            target.addEffect(newPoison);
         }
      }

   }
}
