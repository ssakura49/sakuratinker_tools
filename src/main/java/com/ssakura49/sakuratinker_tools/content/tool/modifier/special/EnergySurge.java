package com.ssakura49.sakuratinker_tools.content.tool.modifier.special;

import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EnergySurge extends BaseModifier {
   public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey keys, TooltipFlag flag) {
      int var10001 = entry.getLevel();
      tooltip.add(Component.literal("传输速度: +" + var10001 * 5000 + " FE/t"));
   }
}
