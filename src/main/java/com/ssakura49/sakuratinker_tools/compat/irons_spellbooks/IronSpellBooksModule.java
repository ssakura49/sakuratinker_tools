package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks;

import com.ssakura49.sakuratinker_tools.compat.CompatManager;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.eventhandler.SpellBookHandler;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.eventhandler.SpellStackHandler;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSModifier;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.material.ISSMaterialRegistry;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.tool.stat.ISSToolStats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class IronSpellBooksModule implements CompatManager.ICompatModule {
   public void register(IEventBus bus) {
      ISSItem.init(bus);
      ISSModifier.MODIFIERS.register(bus);
      SpellBookHandler.init();
      bus.addListener(this::commonSetup);
      DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SpellStackHandler.init(bus));
   }

   public void commonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(ISSMaterialRegistry::init);
      event.enqueueWork(ISSToolStats::init);
      event.enqueueWork(ISSConfig::load);
   }
}
