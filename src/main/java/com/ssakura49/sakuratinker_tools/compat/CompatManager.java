package com.ssakura49.sakuratinker_tools.compat;

import com.ssakura49.sakuratinker_tools.STTConfig;
import com.ssakura49.sakuratinker_tools.util.SafeClass;
import lombok.Getter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

import java.util.function.Supplier;

public class CompatManager {
   private enum Modules {
      ISS(SafeClass.ISS, "com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.IronSpellBooksModule",
              STTConfig.COMPAT.ENABLE_ISS_COMPAT::get),
      GOETY(SafeClass.GOETY, "com.ssakura49.sakuratinker_tools.compat.goety.GoetyModule",
              STTConfig.COMPAT.ENABLE_GOETY_COMPAT::get),
      EXTRABOTANY(SafeClass.ExtraBotany, "com.ssakura49.sakuratinker_tools.compat.extrabotany.ExtraBotanyModule",
              STTConfig.COMPAT.ENABLE_EXTRABOTANY_COMPAT::get),
      WIZARDRY(SafeClass.Wizardry, "com.ssakura49.sakuratinker_tools.compat.wizardry.WizardryModule",
              STTConfig.COMPAT.ENABLE_WIZARDRY_COMPAT::get)
      ;

      @Getter
      private final String modId;
      @Getter
      private final String moduleClassName;
      private final Supplier<Boolean> configSupplier;

      Modules(String modId, String moduleClassName, Supplier<Boolean> configSupplier) {
         this.modId = modId;
         this.moduleClassName = moduleClassName;
         this.configSupplier = configSupplier;
      }

       public boolean isConfigEnabled() { return configSupplier.get(); }
   }

   public static void initAll(IEventBus modBus) {
      for (Modules entry : Modules.values()) {
         boolean isLoaded = isModLoaded(entry.getModId());
         boolean isEnabled = entry.isConfigEnabled();

         if (isLoaded && isEnabled) {
            try {
               Class<?> clazz = Class.forName(entry.getModuleClassName());
               ICompatModule module = (ICompatModule) clazz.getDeclaredConstructor().newInstance();
               module.register(modBus);

               System.out.println("[SakuraTinkerTools] 联动模块加载成功: " + entry.getModId());

            } catch (Throwable t) {
               System.err.println("[SakuraTinkerTools] 联动模块加载失败: " + entry.getModId());
               t.printStackTrace();
            }
         } else {
            if (!isLoaded) {
               // System.out.println("[SakuraTinkerTools] 未检测到 Mod: " + entry.getModId() + "，跳过联动");
            } else {
               System.out.println("[SakuraTinkerTools] 玩家已在配置中禁用联动: " + entry.getModId());
            }
         }
      }
   }
   private static boolean isModLoaded(String modId) {
      return ModList.get().isLoaded(modId);
   }

   @FunctionalInterface
   public interface ICompatModule {
      void register(IEventBus modBus);
   }
}
