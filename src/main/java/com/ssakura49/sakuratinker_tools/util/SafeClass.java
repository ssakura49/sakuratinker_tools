package com.ssakura49.sakuratinker_tools.util;

import com.ssakura49.sakura_library.util.EarlyConfig;
import net.minecraftforge.fml.ModList;

public class SafeClass {
   private static int ISSLoaded = -1;
   public static String ISS = "irons_spellbooks";

   private static int GOETYLoaded = -1;
   public static String GOETY = "goety";

   private static int ExtraBotanyLoaded = -1;
   public static String ExtraBotany = "extrabotany";

   private static int WizardryLoaded = -1;
   public static String Wizardry = "ebwizardry";

   public static boolean isISSLoaded() {
      if (ISSLoaded == -1) {
         ISSLoaded = !EarlyConfig.modIds.contains(ISS) && !ModList.get().isLoaded(ISS) ? 2 : 1;
      }

      return ISSLoaded == 1;
   }
   public static boolean isGOETYLoaded() {
      if (GOETYLoaded == -1) {
         GOETYLoaded = !EarlyConfig.modIds.contains(GOETY) && !ModList.get().isLoaded(GOETY) ? 2 : 1;
      }

      return GOETYLoaded == 1;
   }
   public static boolean isExtraBotanyLoaded() {
      if (ExtraBotanyLoaded == -1) {
         ExtraBotanyLoaded = !EarlyConfig.modIds.contains(ExtraBotany) && !ModList.get().isLoaded(ExtraBotany) ? 2 : 1;
      }

      return ExtraBotanyLoaded == 1;
   }

   public static boolean isWizardryLoaded() {
      if (WizardryLoaded == -1) {
         WizardryLoaded = !EarlyConfig.modIds.contains(Wizardry) && !ModList.get().isLoaded(Wizardry) ? 2 : 1;
      }

      return WizardryLoaded == 1;
   }
}
