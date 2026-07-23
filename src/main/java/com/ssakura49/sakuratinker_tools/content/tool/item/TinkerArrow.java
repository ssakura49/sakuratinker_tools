package com.ssakura49.sakuratinker_tools.content.tool.item;

import com.ssakura49.sakuratinker.STConfig.Common;
import com.ssakura49.sakuratinker.core.library.interfaces.projectile.IProjectileBuild;
import com.ssakura49.sakuratinker_tools.content.entity.TinkerArrowEntity;
import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableArrowItem;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class TinkerArrow extends ModifiableArrowItem implements IProjectileBuild {
   public TinkerArrow(Item.Properties properties, ToolDefinition toolDefinition) {
      super(properties, toolDefinition);
   }

   public @NotNull AbstractArrow m_6394_(Level level, ItemStack stack, LivingEntity entity) {
      TinkerArrowEntity arrow = (TinkerArrowEntity)this.createProjectile(stack, level, entity);
      this.addInfoToProjectile(arrow, stack, level, entity);
      float baseDamage = 1.0F;
      double fixes = (Double)Common.TINKER_ARROW_FIXES.get();
      arrow.setFixes((float)fixes);
      arrow.setTool(stack);
      return arrow;
   }

   public Projectile createProjectile(ItemStack tool, Level level, LivingEntity entity) {
      return new TinkerArrowEntity(level, entity);
   }

   public void addInfoToProjectile(Projectile projectile, ItemStack tool, Level level, LivingEntity entity) {
      if (projectile instanceof TinkerArrowEntity tinkerArrow) {
         tinkerArrow.setTool(tool);
      }

   }

   private List<Component> getTinkerArrowStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
      TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
      if (tool.hasTag(Items.DURABILITY)) {
         builder.addDurability();
      }

      builder.add(ToolStats.ATTACK_DAMAGE);
      builder.add(ToolStats.VELOCITY);
      builder.add(ToolStats.ACCURACY);
      builder.addAllFreeSlots();

      for(ModifierEntry entry : tool.getModifierList()) {
         ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
      }

      return tooltips;
   }

   public List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
      return this.getTinkerArrowStats(tool, player, tooltips, key, flag);
   }
}
