package com.ssakura49.sakuratinker_tools.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.ssakura49.sakuratinker.core.init.STCrKeys;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class STTKeys {

    public static final String RELOAD_KEY_ID = "key.sakuratinker.reload";

    public static void registerKeyBindings() {
        STCrKeys.registerKey(
                RELOAD_KEY_ID,
                GLFW.GLFW_KEY_G,
                STCrKeys.CATEGORY
        );
    }

    public static KeyMapping getReloadKey() {
        return STCrKeys.getMapping(RELOAD_KEY_ID);
    }
}
