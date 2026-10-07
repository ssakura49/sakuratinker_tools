package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.eventhandler;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client.SpellBookStackOverlay;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client.SpellStackKeybinds;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

public class SpellStackHandler {
    public static void init(IEventBus bus) {
        bus.addListener(SpellStackHandler.ModBus::registerKeys);
        bus.addListener(SpellStackHandler.ModBus::registerOverlays);
        MinecraftForge.EVENT_BUS.addListener(SpellStackHandler.ForgeBus::onKeyInput);
    }

    public static final class ModBus {
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            for (KeyMapping mapping : SpellStackKeybinds.all()) {
                event.register(mapping);
            }
        }
        public static void registerOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "spell_book_stack",
                    SpellBookStackOverlay::render);
        }
    }

    public static final class ForgeBus {
        public static void onKeyInput(InputEvent.Key event) {
            SpellStackKeybinds.handleInput(event.getAction());
        }
    }
}
