package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client;

import com.ssakura49.sakura_library.api.client.hud.HudElement;
import com.ssakura49.sakuratinker_tools.STTConfig;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;


@OnlyIn(Dist.CLIENT)
public final class SpellStackHudElement implements HudElement {
    private static final int CARD_SIZE = 22;
    private static final int INFO_GAP = 13;
    private static final int INFO_WIDTH = 150;

    @Override
    public int getWidth() {
        return CARD_SIZE + INFO_GAP + INFO_WIDTH;
    }

    @Override
    public int getHeight() {
        return CARD_SIZE;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        SpellBookStackOverlay.renderAt(graphics, x, y, partialTick);
    }

    @Override
    public boolean isVisible() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || player.isSpectator()) {
            return false;
        }
        if (!STTConfig.COMPAT.ENABLE_ISS_COMPAT_UI.get()) {
            return false;
        }
        return !ClientMagicData.getSpellSelectionManager().getAllSpells().isEmpty();
    }
}
