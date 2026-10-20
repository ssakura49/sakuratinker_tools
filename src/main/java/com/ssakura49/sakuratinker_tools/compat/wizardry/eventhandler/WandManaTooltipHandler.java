package com.ssakura49.sakuratinker_tools.compat.wizardry.eventhandler;

import com.binaris.wizardry.api.content.item.IManaItem;
import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.IModifiable;

import java.util.List;

@Mod.EventBusSubscriber(modid = SakuraTinkerTools.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class WandManaTooltipHandler {
    private static final String MANA_KEY = "item.ebwizardry.wand.damage_desc";

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof IManaItem manaItem)) {
            return;
        }
        if (!(stack.getItem() instanceof IModifiable)) {
            return;
        }
        List<Component> tooltip = event.getToolTip();
        if (tooltip == null) {
            return;
        }
        tooltip.removeIf(line -> line.getContents() instanceof TranslatableContents contents
                && MANA_KEY.equals(contents.getKey()));
        tooltip.add(Component.translatable(MANA_KEY, manaItem.getMana(stack), manaItem.getManaCapacity(stack))
                .withStyle(ChatFormatting.BLUE));
    }

}
