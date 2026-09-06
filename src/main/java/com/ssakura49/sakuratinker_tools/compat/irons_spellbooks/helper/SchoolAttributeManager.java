package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.helper;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public class SchoolAttributeManager {
   private static final Map<ResourceLocation, ResourceLocation> POWER_MAP = new ConcurrentHashMap<>();

   public static void registerAttributes(Map<String, String> rawMap) {
      POWER_MAP.clear();
      if (rawMap != null) {
         rawMap.forEach((k, v) -> {
            try {
               POWER_MAP.put(ResourceLocation.parse(k), ResourceLocation.parse(v));
            } catch (Exception var3) {
               System.err.println("[SakuraTinker] 无效的属性配置: " + k + " -> " + v);
            }

         });
      }
   }

   public static Attribute getPowerAttribute(SchoolType school) {
      if (school == null) {
         return AttributeRegistry.SPELL_POWER.get();
      } else {
         ResourceLocation schoolId = school.getId();
         ResourceLocation attrId = POWER_MAP.get(schoolId);
         if (attrId != null) {
            Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(attrId);
            if (attr != null) {
               return attr;
            }
         }

         ResourceLocation autoId = ResourceLocation.fromNamespaceAndPath(schoolId.getNamespace(), schoolId.getPath() + "_spell_power");
         Attribute autoAttr = ForgeRegistries.ATTRIBUTES.getValue(autoId);
         return autoAttr != null ? autoAttr : AttributeRegistry.SPELL_POWER.get();
      }
   }
}
