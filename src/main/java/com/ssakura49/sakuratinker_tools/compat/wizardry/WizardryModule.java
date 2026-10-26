package com.ssakura49.sakuratinker_tools.compat.wizardry;

import com.ssakura49.sakuratinker_tools.compat.CompatManager;
import com.ssakura49.sakuratinker_tools.compat.wizardry.eventhandler.ElementAffinityEventHandler;
import com.ssakura49.sakuratinker_tools.compat.wizardry.eventhandler.WandManaTooltipHandler;
import com.ssakura49.sakuratinker_tools.compat.wizardry.init.WizardryItem;
import com.ssakura49.sakuratinker_tools.compat.wizardry.init.WizardryModifier;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.stats.WizardryToolStats;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;

public class WizardryModule implements CompatManager.ICompatModule {
    @Override
    public void register(IEventBus modBus) {
        WizardryItem.init(modBus);
        WizardryModifier.MODIFIERS.register(modBus);
        ElementAffinityEventHandler.init();
        modBus.addListener(this::onRegister);
        MinecraftForge.EVENT_BUS.addListener(WandManaTooltipHandler::onItemTooltip);
    }

    public void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            WizardryToolStats.initStats();
        }
    }
}
