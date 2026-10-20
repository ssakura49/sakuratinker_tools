package com.ssakura49.sakuratinker_tools.compat.wizardry.library.tier;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.STTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TinkerWandTiers {
    public static final int COUNT = 8;

    public static final ResourceLocation TIER_KEY = SakuraTinker.getResource("wand_tier");

    private static final String[] IDS = {
            "novice", "apprentice", "artisan", "craftsman",
            "renowned", "grandmaster", "heavenly", "divine"
    };

    private static final int[] SPELL_LEVELS = {0, 0, 1, 1, 2, 2, 3, 3};

    private static final ChatFormatting[] COLORS = {
            ChatFormatting.GRAY, ChatFormatting.WHITE, ChatFormatting.GREEN, ChatFormatting.DARK_AQUA,
            ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD
    };

    private static volatile List<TinkerWandTier> built;

    private TinkerWandTiers() {
    }

    public static List<TinkerWandTier> all() {
        List<TinkerWandTier> local = built;
        if (local == null) {
            synchronized (TinkerWandTiers.class) {
                local = built;
                if (local == null) {
                    local = Collections.unmodifiableList(build());
                    built = local;
                }
            }
        }
        return local;
    }

    public static TinkerWandTier byIndex(int index) {
        List<TinkerWandTier> list = all();
        return index >= 0 && index < list.size() ? list.get(index) : null;
    }

    public static TinkerWandTier byId(ResourceLocation id) {
        if (id == null) {
            return null;
        }
        for (TinkerWandTier tier : all()) {
            if (tier.getId().equals(id)) {
                return tier;
            }
        }
        return null;
    }

    public static TinkerWandTier byIdString(String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        return byId(ResourceLocation.tryParse(raw));
    }

    public static TinkerWandTier first() {
        return all().get(0);
    }

    public static int indexOf(com.binaris.wizardry.api.content.spell.SpellTier tier) {
        if (tier instanceof TinkerWandTier tinkerTier) {
            return tinkerTier.getIndex();
        }
        return 0;
    }

    private static List<TinkerWandTier> build() {
        double manaBase = STTConfig.COMPAT.WIZARDRY_TIER_MANA_BASE.get();
        double manaPeak = STTConfig.COMPAT.WIZARDRY_TIER_MANA_PEAK.get();
        double manaCurve = STTConfig.COMPAT.WIZARDRY_TIER_MANA_CURVE.get();
        int progressionTotal = STTConfig.COMPAT.WIZARDRY_TIER_PROGRESSION_TOTAL.get();
        double progressionCurve = STTConfig.COMPAT.WIZARDRY_TIER_PROGRESSION_CURVE.get();
        int upgradeBase = STTConfig.COMPAT.WIZARDRY_TIER_UPGRADE_BASE.get();
        int upgradeStep = STTConfig.COMPAT.WIZARDRY_TIER_UPGRADE_STEP.get();

        List<TinkerWandTier> list = new ArrayList<>(COUNT);
        double last = COUNT - 1;
        for (int i = 0; i < COUNT; i++) {
            double t = i / last;
            int mana = (int) Math.max(1, Math.round(manaBase + (manaPeak - manaBase) * Math.pow(t, manaCurve)));
            int progression = (int) Math.max(0, Math.round(progressionTotal * Math.pow(t, progressionCurve)));
            int upgradeLimit = upgradeBase + upgradeStep * i;
            ResourceLocation id = SakuraTinker.getResource("tinker_" + IDS[i]);
            list.add(new TinkerWandTier(id, i, COUNT, mana, upgradeLimit,
                    SPELL_LEVELS[i], COLORS[i], progression));
        }
        return list;
    }
}
