package com.ssakura49.sakuratinker_tools.content.tool.item;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class AlchemicalGlovesItem extends ModifiableItem {
   public AlchemicalGlovesItem(Item.Properties properties, ToolDefinition toolDefinition) {
      super(properties, toolDefinition);
   }

   private List<Component> getAlchemicalGlovesStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
      TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
      if (tool.hasTag(Items.DURABILITY)) {
         builder.addDurability();
      }

      builder.add(ToolStats.ATTACK_DAMAGE);
      builder.add(ToolStats.ATTACK_SPEED);
      builder.add(ToolStats.VELOCITY);
      builder.add(ToolStats.ACCURACY);
      builder.addAllFreeSlots();

      for(ModifierEntry entry : tool.getModifierList()) {
         ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
      }

      return tooltips;
   }

   public List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
      return this.getAlchemicalGlovesStats(tool, player, tooltips, key, flag);
   }
}
