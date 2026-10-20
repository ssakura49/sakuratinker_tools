package com.ssakura49.sakuratinker_tools.compat.wizardry.library.tier;

import com.binaris.wizardry.api.content.spell.SpellTier;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

public class TinkerWandTier extends SpellTier {
    @Getter
    private final ResourceLocation id;
    @Getter
    private final int index;
    private final int count;
    private final String translationKey;

    public TinkerWandTier(ResourceLocation id, int index, int count, int maxCharge, int upgradeLimit,
                          int spellLevel, ChatFormatting color, int progression) {
        super(maxCharge, upgradeLimit, 1, spellLevel, color, progression);
        this.id = id;
        this.index = index;
        this.count = count;
        this.translationKey = "wand_tier." + id.getNamespace() + "." + id.getPath();
        this.setLocation(id);
    }

    public boolean isMax() {
        return index >= count - 1;
    }

    @Override
    public SpellTier next() {
        TinkerWandTier next = TinkerWandTiers.byIndex(index + 1);
        return next != null ? next : this;
    }

    @Override
    public SpellTier previous() {
        TinkerWandTier previous = TinkerWandTiers.byIndex(index - 1);
        return previous != null ? previous : this;
    }

    @Override
    protected String getOrCreateDescriptionId() {
        return translationKey;
    }

    @Override
    public String getDescriptionId() {
        return translationKey;
    }

}
