package com.ssakura49.sakuratinker_tools.compat.goety.tool.item;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.magic.SpellType;
import com.ssakura49.sakuratinker.core.utils.TooltipUtil;
import com.ssakura49.sakuratinker_tools.compat.goety.library.tool.stat.GoetyToolStats;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.definition.GoetyToolDefinitions;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.item.base.ModifiableWandItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;


public class TinkerWandItem extends ModifiableWandItem implements ISoulRepair {

    public TinkerWandItem(Item.Properties properties, SpellType spellType) {
        super(properties, spellType, GoetyToolDefinitions.TINKER_WAND, 1);
    }

    @Override
    public @NotNull List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }
        builder.add(ToolStats.ATTACK_DAMAGE);
        TooltipUtil.addToolStatTooltip(builder, tool, GoetyToolStats.SOUL_POWER);
        TooltipUtil.addPerToolStatTooltip(builder, tool, GoetyToolStats.SOUL_INCREASE);
        builder.addAllFreeSlots();
        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, flag);
        }
        return tooltips;
    }
}