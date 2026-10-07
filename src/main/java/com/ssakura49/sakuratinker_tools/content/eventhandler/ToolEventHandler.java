package com.ssakura49.sakuratinker_tools.content.eventhandler;

import com.ssakura49.sakuratinker.core.utils.ToolUtil;
import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import com.ssakura49.sakuratinker_tools.data.STTModifierIds;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = SakuraTinkerTools.MODID)
public class ToolEventHandler {
    @SubscribeEvent
    public static void bindModifier(PlayerEvent.ItemCraftedEvent event) {
        ItemStack stack = event.getCrafting();
        if (ToolUtil.checkTool(stack)){
            ToolStack toolStack = ToolStack.from(stack);
            if (toolStack.hasTag(TinkerTags.Items.BOWS)){
                toolStack.addModifier(STTModifierIds.ARROW_AMMO,1);
            }
        }
    }
}
