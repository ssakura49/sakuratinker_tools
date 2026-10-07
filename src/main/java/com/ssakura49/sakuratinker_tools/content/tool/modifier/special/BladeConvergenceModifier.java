package com.ssakura49.sakuratinker_tools.content.tool.modifier.special;

import com.ssakura49.sakura_library.entity.damagesource.AdvancedDamageSource;
import com.ssakura49.sakura_library.entity.damagesource.LegacyDamageSource;
import com.ssakura49.sakura_library.entity.damagesource.PercentageBypassInvulnerableTimeSource;
import com.ssakura49.sakura_library.util.helper.PlayerAdvDataHelper;
import com.ssakura49.sakura_library.util.helper.PlayerAdvDataHelper.AdvancementCache;
import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BladeConvergenceModifier extends BaseModifier {
   public boolean isNoLevels() {
      return true;
   }

   public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
      if (attacker instanceof ServerPlayer player) {
         int total = AdvancementCache.getTotal(player);
         int completed = PlayerAdvDataHelper.get(player);
         float percent = (float)completed / (float)total;
         if (percent <= 0.0F) return source;
         return new AdvancedDamageSource(source.typeHolder(), attacker, attacker, null)
                 .setInvulnerableTimeBypassPercentage(percent)
                 .setBypassInvulnerability();
      } else {
         return source;
      }
   }
}
