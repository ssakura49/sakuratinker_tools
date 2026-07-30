package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.helper.SchoolAttributeManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

public class ISSConfig {
   private static final Path SCHOOL_CONFIG = FMLPaths.CONFIGDIR.get().resolve("ssakura49/school_attributes.json");
   private static final String DEFAULT_SCHOOL_CONFIG = "/data/sakuratinker/config/school_attributes.json";
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   public static void load() {
      try {
         if (!Files.exists(SCHOOL_CONFIG)) {
            Files.createDirectories(SCHOOL_CONFIG.getParent());
            try (InputStream in = SakuraTinkerTools.class.getResourceAsStream(DEFAULT_SCHOOL_CONFIG)) {
               if (in == null) {
                  SakuraTinker.LOGGER.error("未找到默认的 school_attributes.json");
                  return;
               }
               Files.copy(in, SCHOOL_CONFIG);
            }
         }
         String json = Files.readString(SCHOOL_CONFIG);
         RawConfig config = GSON.fromJson(json, RawConfig.class);
         SchoolAttributeManager.registerAttributes(config != null ? config.power_map : null);
         SakuraTinker.LOGGER.info("成功加载 Iron's Spells 属性联动配置");

      } catch (IOException e) {
         SakuraTinker.LOGGER.error("加载 school_attributes.json 时发生 I/O 错误", e);
      }
   }

   private static class RawConfig {
      Map<String, String> power_map;
   }
}
