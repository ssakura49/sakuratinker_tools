package com.ssakura49.sakuratinker_tools.mixin.iron_spell_book;

import com.ssakura49.sakura_library.util.annotation.ModDependsMixin;
import com.ssakura49.sakuratinker_tools.STTConfig;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client.SpellBookStackOverlay;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client.SpellCardAnimation;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Signals scroll-based spell selection to the card animation API. */
@Mixin(value = ClientInputEvents.class, remap = false)
@ModDependsMixin("irons_spellbooks")
public abstract class SpellScrollInputMixin {
    @ModifyArg(method = "handleSpellBarScrollModifier(I)Z",
        at = @At(value = "INVOKE",
            target = "Lio/redspace/ironsspellbooks/api/magic/SpellSelectionManager;makeSelection(I)V"),
        index = 0)
    private static int markScrollSelection(int selectedIndex) {
        if (STTConfig.COMPAT.ENABLE_ISS_COMPAT_UI.get()){
            SpellSelectionManager manager = ClientMagicData.getSpellSelectionManager();
            int previousIndex = manager.getGlobalSelectionIndex();
            SpellCardAnimation.signalScrollSelection(previousIndex, selectedIndex, manager.getSpellCount());
            SpellBookStackOverlay.playSelectionSound(previousIndex, selectedIndex, manager.getSpellCount());
            return selectedIndex;
        }
        return selectedIndex;
    }
}
