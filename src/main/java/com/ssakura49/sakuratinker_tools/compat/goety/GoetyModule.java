package com.ssakura49.sakuratinker_tools.compat.goety;

import com.ssakura49.sakuratinker_tools.compat.CompatManager;
import com.ssakura49.sakuratinker_tools.compat.goety.eventhandler.SpellAttackHandler;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyModifier;
import com.ssakura49.sakuratinker_tools.compat.goety.library.material.GoetyMaterialRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class GoetyModule implements CompatManager.ICompatModule {

   @Override
   public void register(IEventBus modBus) {
      GoetyItem.init(modBus);
      GoetyModifier.MODIFIERS.register(modBus);
      SpellAttackHandler.init();
      modBus.addListener(this::commonSetup);
   }

   public void commonSetup(FMLCommonSetupEvent event) {
      //event.enqueueWork(GoetyMaterialRegistry::init);
   }
}
