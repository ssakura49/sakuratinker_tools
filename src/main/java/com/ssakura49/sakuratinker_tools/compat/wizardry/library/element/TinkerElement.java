package com.ssakura49.sakuratinker_tools.compat.wizardry.library.element;

import com.binaris.wizardry.api.content.spell.Element;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

public class TinkerElement extends Element {
    private final ResourceLocation id;
    private final String descriptionKey;

    public TinkerElement(ResourceLocation id, ChatFormatting color, int... colors) {
        super(color, colors);
        this.id = id;
        this.descriptionKey = "element." + id.getNamespace() + "." + id.getPath();
    }

    @Override
    protected ResourceLocation getOrCreateLocation() {
        return id;
    }

    @Override
    protected String getOrCreateDescriptionId() {
        return descriptionKey;
    }
}
