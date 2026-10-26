package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client;

import com.ssakura49.sakura_library.api.client.hud.HudAnchor;
import com.ssakura49.sakura_library.api.client.hud.HudSlot;
import com.ssakura49.sakura_library.api.client.hud.SLHud;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public final class SpellStackHudRegistrar {
    public static final ResourceLocation SLOT_ID =
            ResourceLocation.fromNamespaceAndPath("sakuratinker_tools", "spell_stack");

    private SpellStackHudRegistrar() {
    }

    public static void register() {
        SLHud.bootstrap();
        SLHud.register(new HudSlot(SLOT_ID, HudAnchor.MIDDLE_LEFT, SpellStackHudElement::new)
                .priority(10)
                .gap(4));
    }
}
