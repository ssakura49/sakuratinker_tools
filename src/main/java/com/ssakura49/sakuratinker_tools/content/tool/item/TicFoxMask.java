package com.ssakura49.sakuratinker_tools.content.tool.item;

import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import com.ssakura49.sakuratinker.core.utils.TooltipUtil;
import com.ssakura49.tinkercuriolib.tools.item.ModifiableCurioItem;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TicFoxMask extends ModifiableCurioItem {
   public TicFoxMask(Item.Properties properties, ToolDefinition toolDefinition) {
      super(properties, toolDefinition);
   }

   public @NotNull List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
      TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
      TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.INVULNERABLE_TIME);
      builder.addAllFreeSlots();

      for(ModifierEntry entry : tool.getModifierList()) {
         ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
      }

      return tooltips;
   }
}
