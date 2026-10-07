package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

/**
 * Key bindings for the Spell Stack UI:
 *  - toggle between the default collapsed stack and a permanently expanded left column;
 *  - quick-select the previous/next spell with dedicated keys (reuses the exact
 *    scroll-selection animation and dial sound path).
 */
@OnlyIn(Dist.CLIENT)
public final class SpellStackKeybinds {
    public static final String CATEGORY = "key.categories.sakuratinker";

    private static final KeyMapping TOGGLE_EXPANDED = new KeyMapping(
        "key.sakuratinker.toggle_expanded", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_N, CATEGORY);
    private static final KeyMapping SELECT_PREVIOUS = new KeyMapping(
        "key.sakuratinker.previous_spell", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, CATEGORY);
    private static final KeyMapping SELECT_NEXT = new KeyMapping(
        "key.sakuratinker.next_spell", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_PERIOD, CATEGORY);

    private SpellStackKeybinds() {
    }

    public static KeyMapping[] all() {
        return new KeyMapping[]{TOGGLE_EXPANDED, SELECT_PREVIOUS, SELECT_NEXT};
    }

    public static void handleInput(int action) {
        if (action != GLFW.GLFW_PRESS) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null || minecraft.player == null) {
            return;
        }
        if (TOGGLE_EXPANDED.consumeClick()) {
            SpellBookStackOverlay.toggleExpandedMode();
            return;
        }
        if (SELECT_PREVIOUS.consumeClick()) {
            quickSelect(-1);
            return;
        }
        if (SELECT_NEXT.consumeClick()) {
            quickSelect(1);
        }
    }

    private static void quickSelect(int direction) {
        if (!SpellBookStackOverlay.isModLoaded()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        SpellSelectionManager manager = ClientMagicData.getSpellSelectionManager();
        int spellCount = manager.getSpellCount();
        if (spellCount <= 0) {
            return;
        }
        int current = Mth.clamp(manager.getGlobalSelectionIndex(), 0, spellCount - 1);
        // circular selection: last -> first, first -> last (Math.floorMod handles negatives)
        int next = Math.floorMod(current + direction, spellCount);
        if (next != current) {
            SpellCardAnimation.signalScrollSelection(current, next, spellCount);
            SpellBookStackOverlay.playSelectionSound(current, next, spellCount);
            manager.makeSelection(next);
        }
    }
}
