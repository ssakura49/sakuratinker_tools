package com.ssakura49.sakuratinker_tools.compat.wizardry.tool.modifier;

import com.ssakura49.sakuratinker.core.library.tools.modifier.hooks.STHooks;
import com.ssakura49.sakuratinker.core.library.tools.modifier.hooks.display.CustomBarDisplayModifierHook;
import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import com.ssakura49.sakuratinker_tools.STTConfig;
import com.ssakura49.sakuratinker_tools.compat.wizardry.utils.WandManaDataHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class WandManaBarModifier extends BaseModifier implements CustomBarDisplayModifierHook {
    private static final String BAR_ID = "sakuratinker:wand_mana";

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, STHooks.CUSTOM_BAR);
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @NotNull String getBarId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return BAR_ID;
    }

    @Override
    public boolean shouldShowBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return barsHadBeenShown == 0
                && STTConfig.COMPAT.WIZARDRY_MANA_BAR.get()
                && tool.getPersistentData().getInt(WandManaDataHelper.CAPACITY) > 0;
    }

    @Override
    public float getBarProgress(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int capacity = tool.getPersistentData().getInt(WandManaDataHelper.CAPACITY);
        if (capacity <= 0) {
            return 0.0F;
        }
        return Mth.clamp((float) tool.getPersistentData().getInt(WandManaDataHelper.MANA) / capacity, 0.0F, 1.0F);
    }

    @Override
    public int getBarColor(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int capacity = tool.getPersistentData().getInt(WandManaDataHelper.CAPACITY);
        float ratio = capacity <= 0 ? 0.0F : Mth.clamp((float) tool.getPersistentData().getInt(WandManaDataHelper.MANA) / capacity, 0.0F, 1.0F);
        return 0xFF000000 | (Mth.hsvToRgb(0.75F - 0.25F * ratio, 0.85F, 1.0F) & 0xFFFFFF);
    }
}
