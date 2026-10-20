package com.ssakura49.sakuratinker_tools.compat.goety;

import com.ssakura49.sakuratinker_tools.compat.CompatManager;
import com.ssakura49.sakuratinker_tools.compat.goety.eventhandler.SpellAttackHandler;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyModifier;
import com.ssakura49.sakuratinker_tools.compat.goety.library.tool.stat.GoetyToolStats;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

public class GoetyModule implements CompatManager.ICompatModule {

   @Override
   public void register(IEventBus modBus) {
      GoetyItem.init(modBus);
      GoetyModifier.MODIFIERS.register(modBus);
      SpellAttackHandler.init();
      modBus.addListener(this::commonSetup);
      modBus.addListener(this::onRegister);
   }

   public void commonSetup(FMLCommonSetupEvent event) {
   }

   public void onRegister(RegisterEvent event) {
      if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
         GoetyToolStats.initStats();
      }

   }
}
