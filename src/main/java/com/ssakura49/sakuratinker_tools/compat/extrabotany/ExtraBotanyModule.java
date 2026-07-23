package com.ssakura49.sakuratinker_tools.compat.extrabotany;

import com.ssakura49.sakuratinker_tools.compat.CompatManager;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.eventhandler.EBClientModEventHandler;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBEntity;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBModifier;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.library.material.ExtraBotanyMaterialRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ExtraBotanyModule implements CompatManager.ICompatModule {
    @Override
    public void register(IEventBus modBus) {
        EBItem.init(modBus);
        EBModifier.MODIFIERS.register(modBus);
        EBEntity.ENTITIES.register(modBus);
        EBClientModEventHandler.init(modBus);
        modBus.addListener(this::commonSetup);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ExtraBotanyMaterialRegistry::init);
    }
}
