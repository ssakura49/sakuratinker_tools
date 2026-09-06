package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ISSReflectionHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String MODID = "irons_spellbooks";
    private static final String SCHOOL_REGISTRY_CLASS = "io.redspace.ironsspellbooks.api.registry.SchoolRegistry";
    private static final String SCHOOL_TYPE_CLASS = "io.redspace.ironsspellbooks.api.magic.SchoolType";
    private static final String IS_STATS_CLASS = "com.ssakura49.sakuratinker_materials.material.stats.iss.ISSToolStats";


    public static boolean isLoaded() {
        return !ModList.get().isLoaded(MODID);
    }

    public static Object getSchoolType(ResourceLocation id) {
        if (isLoaded()) return null;
        try {
            Class<?> registryClass = Class.forName(SCHOOL_REGISTRY_CLASS);
            Method getMethod = registryClass.getMethod("getSchool", ResourceLocation.class);
            return getMethod.invoke(null, id);
        } catch (Exception e) {
            LOGGER.warn("Failed to get SchoolType for ID: " + id);
            return null;
        }
    }

    public static Component getDisplayName(Object schoolType) {
        if (isLoaded() || schoolType == null) {
            return Component.literal("Unknown School");
        }
        try {
            Method method = schoolType.getClass().getMethod("getDisplayName");
            return (Component) method.invoke(schoolType);
        } catch (NoSuchMethodException e) {
            return Component.literal(schoolType.toString());
        } catch (Exception e) {
            LOGGER.warn("Failed to get DisplayName for SchoolType", e);
            return Component.literal(schoolType.toString());
        }
    }

    public static Object getStatObject(Object schoolType) {
        if (isLoaded() || schoolType == null) return null;
        try {
            Class<?> statsClass = Class.forName(IS_STATS_CLASS);
            Field mapField = statsClass.getField("SCHOOL_BONUS");
            Map<?, ?> map = (Map<?, ?>) mapField.get(null);
            return map.get(schoolType);
        } catch (Exception e) {
            LOGGER.warn("Failed to get StatObject for SchoolType", e);
            return null;
        }
    }

    public static void applyStat(Object stat, ModifierStatsBuilder builder, float value) {
        if (stat == null) return;
        try {
            Method updateMethod = stat.getClass().getMethod("update", ModifierStatsBuilder.class, float.class);
            updateMethod.invoke(stat, builder, value);
        } catch (Exception e) {
            LOGGER.warn("Failed to apply Stat", e);
        }
    }
}
