package com.ssakura49.sakuratinker_tools.content.tool.modifier.special;

import com.ssakura49.sakura_library.entity.damagesource.LegacyDamageSource;
import com.ssakura49.sakura_library.entity.damagesource.PercentageBypassArmorSource;
import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SwiftSwordTechniqueModifier extends BaseModifier {
   public boolean isNoLevels() {
      return true;
   }

   public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
      return PercentageBypassArmorSource.Any(source.typeHolder(), target, attacker, 1.0F);
   }
}
