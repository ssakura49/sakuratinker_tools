package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.ISSHooks;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook.SpellDamageModifierHook;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.hook.spellbook.SpellHitModifierHook;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context.SpellAttackContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ToolType;

public class GreedyBookModifier extends BaseModifier implements SpellHitModifierHook, SpellDamageModifierHook {
   private final ResourceLocation ME = SakuraTinker.getResource("magic_enhancement");

   protected void registerHooks(ModuleHookMap.Builder builder) {
      super.registerHooks(builder);
      builder.addHook(this, ISSHooks.SPELL_DAMAGE, ISSHooks.SPELL_HIT);
   }

   public float getSpellDamage(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float baseDamage, float damage) {
      LivingEntity attacker = context.getAttacker();
      TinkerEffect effect = (TinkerEffect)TinkerModifiers.insatiableEffect.get(ToolType.ARMOR);
      int level = TinkerEffect.getAmplifier(attacker, effect);
      if (level >= 0) {
         damage += (float)(level + 1) * 2.0F;
      }

      return damage;
   }

   public void afterSpellHit(IToolStackView tool, ModifierEntry modifier, SpellAttackContext context, float damageDealt) {
      LivingEntity attacker = context.getAttacker();
      if (!attacker.level.isClientSide) {
         int maxLevel = modifier.getLevel() - 1;
         int duration = 60;
         this.applyGreedyEffect(attacker, ToolType.ARMOR, duration, 1, maxLevel);
      }
   }

   private void applyGreedyEffect(LivingEntity living, ToolType type, int duration, int add, int maxLevel) {
      TinkerEffect effect = (TinkerEffect)TinkerModifiers.insatiableEffect.get(type);
      int currentLevel = TinkerEffect.getAmplifier(living, effect);
      int nextLevel = currentLevel < 0 ? 0 : Math.min(maxLevel, currentLevel + add);
      effect.apply(living, duration, nextLevel, true);
   }
}
