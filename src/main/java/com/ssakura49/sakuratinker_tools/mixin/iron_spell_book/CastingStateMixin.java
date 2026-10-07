package com.ssakura49.sakuratinker_tools.mixin.iron_spell_book;

import com.ssakura49.sakura_library.util.annotation.ModDependsMixin;
import com.ssakura49.sakuratinker_tools.STTConfig;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client.SpellCardAnimation;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = ClientMagicData.class, remap = false)
@ModDependsMixin("irons_spellbooks")
public abstract class CastingStateMixin {
    @Inject(method = "setClientCastState(Ljava/lang/String;IILio/redspace/ironsspellbooks/api/spells/CastSource;Ljava/lang/String;)V",
        at = @At("RETURN"))
    private static void onCastStarted(String spellId, int spellLevel, int castDuration,
            CastSource castSource, String castingEquipmentSlot, CallbackInfo ci) {
        if (STTConfig.COMPAT.ENABLE_ISS_COMPAT_UI.get()) {
            SpellCardAnimation.signalCastingStarted();
        }
    }

    @Inject(method = "resetClientCastState(Ljava/util/UUID;)V", at = @At("RETURN"))
    private static void onCastStopped(UUID casterId, CallbackInfo ci) {
        if (STTConfig.COMPAT.ENABLE_ISS_COMPAT_UI.get()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getUUID().equals(casterId)) {
                SpellCardAnimation.signalCastingStopped();
            }
        }


    }
}
