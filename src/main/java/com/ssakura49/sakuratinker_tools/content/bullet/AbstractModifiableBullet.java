package com.ssakura49.sakuratinker_tools.content.bullet;

import com.ssakura49.sakuratinker.core.utils.AttackUtil;
import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

/**
 * 继承 {@link AbstractBullet}（弹道/衰减/碰撞逻辑完全复用），仅替换伤害结算：
 * 命中走{@link AttackUtil#attackEntity}
 * 方块穿透：按弹药工具收获等级（HARVEST_TIER）判定，等级 &gt;= 方块等级 + 2 时可直接穿透。
 */
@Getter
public abstract class AbstractModifiableBullet extends AbstractBullet {
   protected final ToolStack tool;

   protected AbstractModifiableBullet(
      Level level,
      LivingEntity shooter,
      ToolStack tool,
      Vec3 start,
      Vec3 direction,
      float damage,
      float penetrationDrop,
      boolean isCrit,
      float projectileSpeed,
      float gravityPerTick,
      float falloffStart,
      float falloffEnd
   ) {
      super(level, shooter, start, direction, damage, penetrationDrop, isCrit, projectileSpeed, gravityPerTick, falloffStart, falloffEnd);
      this.tool = tool;
   }

   @Override
   protected boolean dealDamageTo(LivingEntity target, float damage, boolean useExplosion) {
      return AttackUtil.attackEntity(
         this.tool, this.shooter, InteractionHand.MAIN_HAND, target,
         () -> 1.0, false, EquipmentSlot.MAINHAND,
         true, damage, 1.0F, true
      );
   }

   @Override
   protected boolean canPenetrateBlock(BlockState state) {
      int toolLevel = ((Tier)this.tool.getStats().get(ToolStats.HARVEST_TIER)).getLevel();
      return getBlockHarvestLevel(state) <= toolLevel - 2;
   }

   private int getBlockHarvestLevel(BlockState state) {
      if (state.is(net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL)) {
         return 3;
      } else if (state.is(net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL)) {
         return 2;
      } else {
         return state.is(net.minecraft.tags.BlockTags.NEEDS_STONE_TOOL) ? 1 : 0;
      }
   }
}
