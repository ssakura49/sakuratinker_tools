package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.KeyMappings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;


@OnlyIn(Dist.CLIENT)
public final class SpellStackKeybinds {
    public static final String CATEGORY = "key.categories.sakuratinker";

    private static final KeyMapping TOGGLE_EXPANDED = new KeyMapping(
        "key.sakuratinker.toggle_expanded", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_N, CATEGORY);

    private static final KeyMapping SCROLL_MODIFIER = new KeyMapping(
        "key.sakuratinker.scroll_modifier", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL, CATEGORY);

    private SpellStackKeybinds() {
    }

    public static KeyMapping[] all() {
        return new KeyMapping[]{TOGGLE_EXPANDED, SCROLL_MODIFIER};
    }


    public static KeyMapping scrollModifierKey() {
        return SCROLL_MODIFIER;
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
        }
    }

    public static boolean handleScroll(double delta) {
        if (!SCROLL_MODIFIER.isDown()) {
            return false;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null || minecraft.player == null) {
            return false;
        }
        if (KeyMappings.SPELLBAR_SCROLL_MODIFIER_KEYMAP.isDown()) {
            return false;
        }
        if (delta == 0.0D) {
            return false;
        }
        quickSelect(delta > 0.0D ? -1 : 1);
        return true;
    }

    private static void quickSelect(int direction) {
        if (!SpellBookStackOverlay.isModLoaded()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        SpellSelectionManager manager = ClientMagicData.getSpellSelectionManager();
        int spellCount = manager.getSpellCount();
        if (spellCount <= 0) {
            return;
        }
        int current = Mth.clamp(manager.getGlobalSelectionIndex(), 0, spellCount - 1);
        int next = Math.floorMod(current + direction, spellCount);
        if (next != current) {
            SpellCardAnimation.signalScrollSelection(current, next, spellCount);
            SpellBookStackOverlay.playSelectionSound(current, next, spellCount);
            manager.makeSelection(next);
        }
    }
}
