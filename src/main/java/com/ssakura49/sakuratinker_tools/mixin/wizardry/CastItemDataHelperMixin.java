package com.ssakura49.sakuratinker_tools.mixin.wizardry;

import com.binaris.wizardry.api.content.item.IWorkbenchItem;
import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.api.content.util.CastItemDataHelper;
import com.binaris.wizardry.setup.registries.Spells;
import com.ssakura49.sakura_library.util.annotation.ModDependsMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@ModDependsMixin("ebwizardry")
@Mixin(value = CastItemDataHelper.class, remap = false)
public abstract class CastItemDataHelperMixin {
    @Inject(method = "getSpells", at = @At("HEAD"), cancellable = true, remap = false)
    private static void paddedGetSpells(ItemStack stack, CallbackInfoReturnable<List<Spell>> cir) {
        CompoundTag tag = stack.getTag();
        if (tag == null || tag.isEmpty()) {
            List<Spell> spells = new ArrayList<>();
            int maxSlots = stack.getItem() instanceof IWorkbenchItem workbenchItem ? workbenchItem.getSpellSlotCount(stack) : 5;
            while (spells.size() < maxSlots) {
                spells.add(Spells.NONE);
            }
            cir.setReturnValue(spells);
        }
    }
}
