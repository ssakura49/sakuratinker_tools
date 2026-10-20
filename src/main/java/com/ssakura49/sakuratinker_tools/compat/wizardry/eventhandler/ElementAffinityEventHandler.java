package com.ssakura49.sakuratinker_tools.compat.wizardry.eventhandler;

import com.binaris.wizardry.api.content.event.SpellCastEvent;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.core.event.WizardryEventBus;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.modifier.hook.ElementAffinityModifierHook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public final class ElementAffinityEventHandler {
    private ElementAffinityEventHandler() {
    }

    public static void init() {
        WizardryEventBus.getInstance().register(SpellCastEvent.Pre.class, ElementAffinityEventHandler::onPreCast);
    }

    private static void onPreCast(SpellCastEvent.Pre event) {
        if (!(event.getContext() instanceof PlayerCastContext ctx)) {
            return;
        }
        Player caster = ctx.caster();
        if (caster == null) {
            return;
        }
        ItemStack stack = caster.getItemInHand(ctx.hand());
        if (stack.isEmpty() || !(stack.getItem() instanceof IModifiable)) {
            return;
        }
        float affinity = ElementAffinityModifierHook.get(ToolStack.from(stack), event.getSpell().getElement());
        if (affinity != 0.0F) {
            event.getModifiers().multiplyTotal(SpellModifiers.POTENCY, 1.0F + affinity);
        }
    }
}
