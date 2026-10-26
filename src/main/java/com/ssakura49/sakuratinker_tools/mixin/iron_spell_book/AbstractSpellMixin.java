package com.ssakura49.sakuratinker_tools.mixin.iron_spell_book;

import com.ssakura49.sakura_library.util.annotation.ModDependsMixin;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.school.SchoolOverrideContext;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = AbstractSpell.class, remap = false)
@ModDependsMixin("irons_spellbooks")
public abstract class AbstractSpellMixin {
    @Inject(method = "getSchoolType", at = @At("RETURN"), cancellable = true)
    private void sakuratinker$overrideSchool(CallbackInfoReturnable<SchoolType> cir) {
        SchoolType override = SchoolOverrideContext.peek();
        if (override != null) {
            cir.setReturnValue(override);
        }
    }

    @Inject(
        method = "castSpell(Lnet/minecraft/world/level/Level;ILnet/minecraft/server/level/ServerPlayer;Lio/redspace/ironsspellbooks/api/spells/CastSource;Z)V",
        at = @At("RETURN")
    )
    private void sakuratinker$popSchoolOverride(CallbackInfo ci) {
        SchoolOverrideContext.pop();
    }
}
