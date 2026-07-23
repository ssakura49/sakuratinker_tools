package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.tool.stat;

import com.ssakura49.sakuratinker.SakuraTinker;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class ISSToolStats {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final Map<SchoolType, FloatToolStat> SCHOOL_BONUS = new HashMap<>();
   private static boolean isInitialized = false;
   public static final FloatToolStat SPELL_SLOT = (FloatToolStat)ToolStats.register(new FloatToolStat(name("spell_slot"), -10887823, 1.0F, 1.0F, 15.0F));
   public static final FloatToolStat PERCENT_BONUS = (FloatToolStat)ToolStats.register(new FloatToolStat(name("percent_bonus"), -2661276, 0.0F, 0.0F, Float.MAX_VALUE));
   public static final FloatToolStat MANA_REDUCE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("mana_reduce"), -2661276, 0.0F, 0.0F, Float.MAX_VALUE));
   public static final FloatToolStat MANA_REGEN = (FloatToolStat)ToolStats.register(new FloatToolStat(name("mana_regen"), -2661276, 0.0F, 0.0F, Float.MAX_VALUE));
   public static final FloatToolStat CAST_TIME_REDUCE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("cast_time_reduce"), -2661276, 0.0F, 0.0F, Float.MAX_VALUE));
   public static final FloatToolStat SPELL_DAMAGE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("spell_damage"), -2661276, 0.0F, 0.0F, Float.MAX_VALUE));
   public static final FloatToolStat MANA_VALUE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("mana_value"), -2661276, 0.0F, 0.0F, Float.MAX_VALUE));

   public static void init() {
      if (!isInitialized) {
         try {
            if (SchoolRegistry.REGISTRY == null) {
               LOGGER.error("[SakuraTinker] Iron's Spells Registry is NOT present yet! Skipping School Stats init.");
               return;
            }

            Collection<SchoolType> schools = (SchoolRegistry.REGISTRY.get()).getValues();
            if (schools.isEmpty()) {
               LOGGER.warn("[SakuraTinker] Iron's Spells Registry is empty during init. Are schools not registered yet?");
            }

            for(SchoolType school : schools) {
               registerSchool(school);
            }

            isInitialized = true;
            LOGGER.info("[SakuraTinker] Successfully initialized {} school tool stats.", SCHOOL_BONUS.size());
         } catch (Exception e) {
            LOGGER.error("[SakuraTinker] Failed to initialize ISSToolStats!", e);
         }

      }
   }

   private static void registerSchool(SchoolType school) {
      if (school != null && school.getId() != null) {
         if (!SCHOOL_BONUS.containsKey(school)) {
            try {
               FloatToolStat stat = (FloatToolStat)ToolStats.register(new FloatToolStat(name("school_bonus_" + school.getId().getPath()), -2661276, 0.0F, 0.0F, Float.MAX_VALUE));
               SCHOOL_BONUS.put(school, stat);
            } catch (Exception e) {
               LOGGER.error("[SakuraTinker] Error registering tool stat for school: {}", school.getId(), e);
            }

         }
      }
   }

   public static Map<SchoolType, Float> getSchoolBonuses(IToolStackView tool) {
      Map<SchoolType, Float> map = new HashMap<>();

      for(Map.Entry<SchoolType, FloatToolStat> entry : SCHOOL_BONUS.entrySet()) {
         float value = tool.getStats().get(entry.getValue());
         if (value != 0.0F) {
            map.put(entry.getKey(), value);
         }
      }

      return map;
   }

   private static ToolStatId name(String name) {
      return new ToolStatId(SakuraTinker.getResource(name));
   }
}
