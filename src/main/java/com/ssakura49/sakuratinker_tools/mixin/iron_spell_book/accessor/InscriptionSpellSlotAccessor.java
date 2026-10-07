package com.ssakura49.sakuratinker_tools.mixin.iron_spell_book.accessor;

import com.ssakura49.sakura_library.util.annotation.ModDependsMixin;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableScreen$SpellSlotInfo", remap = false)
@ModDependsMixin("irons_spellbooks")
public interface InscriptionSpellSlotAccessor {
    @Accessor("relativePosition")
    void setRelativePosition(Vec2 relativePosition);

    @Accessor("button")
    Button getButton();
}
