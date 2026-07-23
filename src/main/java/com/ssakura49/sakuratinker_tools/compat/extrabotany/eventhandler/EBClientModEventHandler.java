package com.ssakura49.sakuratinker_tools.compat.extrabotany.eventhandler;

import com.ssakura49.sakuratinker_tools.compat.extrabotany.client.PhantomSwordRenderer;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EBClientModEventHandler {
    public static void init(IEventBus bus) {
        bus.addListener(EBClientModEventHandler::onRegisterRenderers);
    }

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EBEntity.PHANTOM_SWORD.get(), PhantomSwordRenderer::new);
    }
}
