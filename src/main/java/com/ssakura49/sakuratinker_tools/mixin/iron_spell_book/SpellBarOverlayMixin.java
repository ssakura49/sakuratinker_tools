package com.ssakura49.sakuratinker_tools.mixin.iron_spell_book;

import com.ssakura49.sakura_library.util.annotation.ModDependsMixin;
import com.ssakura49.sakuratinker_tools.STTConfig;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Disables the vanilla ISS spell bar so our card stack replaces it. */
@Mixin(value = SpellBarOverlay.class, remap = false)
@ModDependsMixin("irons_spellbooks")
public abstract class SpellBarOverlayMixin {
    @Inject(method = "render(Lnet/minecraftforge/client/gui/overlay/ForgeGui;Lnet/minecraft/client/gui/GuiGraphics;FII)V",
        at = @At("HEAD"), cancellable = true)
    private void cancelVanillaSpellBar(ForgeGui gui, GuiGraphics graphics, float partialTick,
            int screenWidth, int screenHeight, CallbackInfo ci) {
        if (STTConfig.COMPAT.ENABLE_ISS_COMPAT_UI.get()){
            ci.cancel();
        }
    }
}
