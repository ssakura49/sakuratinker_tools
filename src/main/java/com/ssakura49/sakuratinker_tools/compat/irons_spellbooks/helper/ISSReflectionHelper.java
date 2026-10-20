package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.helper;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.tool.stat.ISSToolStats;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import javax.annotation.Nullable;


public class ISSReflectionHelper {
    private static final String MODID = "irons_spellbooks";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MODID);
    }

    @Nullable
    public static SchoolType getSchoolType(ResourceLocation id) {
        if (!isLoaded() || id == null) {
            return null;
        }
        return SchoolRegistry.getSchool(id);
    }

    @Nullable
    public static Component getDisplayName(@Nullable SchoolType school) {
        if (!isLoaded() || school == null) {
            return null;
        }
        return school.getDisplayName();
    }

    @Nullable
    public static FloatToolStat getStatObject(@Nullable SchoolType school) {
        if (!isLoaded()) {
            return null;
        }
        return ISSToolStats.getSchoolStat(school);
    }

    public static void applyStat(@Nullable FloatToolStat stat, ModifierStatsBuilder builder, float value) {
        if (stat == null) {
            return;
        }
        stat.update(builder, value);
    }
}
