package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.eventhandler;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client.SpellStackHudRegistrar;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client.SpellStackKeybinds;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SpellStackHandler {
    public static void init(IEventBus bus) {
        bus.addListener(SpellStackHandler.ModBus::registerKeys);
        bus.addListener(SpellStackHandler.ModBus::registerHud);
        MinecraftForge.EVENT_BUS.addListener(SpellStackHandler.ForgeBus::onKeyInput);
        MinecraftForge.EVENT_BUS.addListener(SpellStackHandler.ForgeBus::onMouseScroll);
    }

    public static final class ModBus {
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            for (KeyMapping mapping : SpellStackKeybinds.all()) {
                event.register(mapping);
            }
        }

        public static void registerHud(FMLClientSetupEvent event) {
            event.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> SpellStackHudRegistrar::register));
        }
    }

    public static final class ForgeBus {
        public static void onKeyInput(InputEvent.Key event) {
            SpellStackKeybinds.handleInput(event.getAction());
        }

        public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
            if (SpellStackKeybinds.handleScroll(event.getScrollDelta())) {
                event.setCanceled(true);
            }
        }
    }
}