package com.ssakura49.sakuratinker_tools.compat.extrabotany;

import com.ssakura49.sakuratinker_tools.compat.CompatManager;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.eventhandler.EBClientModEventHandler;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBEntity;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBModifier;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.library.tool.stat.EBToolStats;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

public class ExtraBotanyModule implements CompatManager.ICompatModule {
    @Override
    public void register(IEventBus modBus) {
        EBItem.init(modBus);
        EBModifier.MODIFIERS.register(modBus);
        EBEntity.ENTITIES.register(modBus);
        EBClientModEventHandler.init(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::onRegister);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
    }

    public void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            EBToolStats.initStats();
        }

    }
}
