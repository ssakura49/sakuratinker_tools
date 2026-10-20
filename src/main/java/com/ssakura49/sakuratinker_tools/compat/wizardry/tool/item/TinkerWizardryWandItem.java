package com.ssakura49.sakuratinker_tools.compat.wizardry.tool.item;

import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.ssakura49.sakuratinker.core.utils.TooltipUtil;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.stats.WizardryToolStats;
import com.ssakura49.sakuratinker_tools.compat.wizardry.tool.item.base.ModifiableWizerdryWandItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class TinkerWizardryWandItem extends ModifiableWizerdryWandItem {
    public TinkerWizardryWandItem(Properties properties,SpellTier tier, Element element, ToolDefinition definition) {
        super(properties,tier, element, definition);
    }

    @Override
    public @NotNull List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }
        TooltipUtil.addToolStatTooltip(builder, tool, WizardryToolStats.MANA_CAPACITY);
        builder.addAllFreeSlots();
        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, flag);
        }
        return tooltips;
    }
}
