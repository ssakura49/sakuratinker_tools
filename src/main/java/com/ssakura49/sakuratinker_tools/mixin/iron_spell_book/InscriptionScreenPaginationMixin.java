package com.ssakura49.sakuratinker_tools.mixin.iron_spell_book;

import com.ssakura49.sakura_library.util.annotation.ModDependsMixin;
import com.ssakura49.sakuratinker_tools.mixin.iron_spell_book.accessor.InscriptionSpellSlotAccessor;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;


@Mixin(value = InscriptionTableScreen.class, remap = false)
@ModDependsMixin("irons_spellbooks")
public abstract class InscriptionScreenPaginationMixin extends AbstractContainerScreen<AbstractContainerMenu> {
    @Unique
    private static final int SLOT_SIZE = 19;
    @Unique
    private static final int MAX_COLUMNS = 5;
    @Unique
    private static final int VISIBLE_ROWS = 3;
    @Unique
    private static final int SPELL_AREA_X = 67;
    @Unique
    private static final int SPELL_AREA_Y = 15;
    @Unique
    private static final int SPELL_AREA_WIDTH = 95;
    @Unique
    private static final int SPELL_AREA_HEIGHT = 57;
    @Unique
    private static final int SCROLLBAR_X = 165;
    @Unique
    private static final int SCROLLBAR_WIDTH = 3;
    @Unique
    private static final int VANILLA_IMAGE_WIDTH = 256;

    @Shadow
    protected ArrayList<?> spellSlots;
    @Shadow
    private ItemStack lastSpellBookItem;
    @Unique
    private int sakuratinker_tools$scrollRow;
    @Unique
    private int sakuratinker_tools$maxScrollRow;

    protected InscriptionScreenPaginationMixin(AbstractContainerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Unique
    private static int sakuratinker_tools$getTotalRows(int spellCount) {
        return (spellCount + MAX_COLUMNS - 1) / MAX_COLUMNS;
    }

    @Unique
    private static int sakuratinker_tools$getRowForIndex(int index, int spellCount) {
        int totalRows = sakuratinker_tools$getTotalRows(spellCount);
        int baseRowSize = spellCount / totalRows;
        int largerRows = spellCount % totalRows;
        int firstIndex = 0;
        for (int row = 0; row < totalRows; row++) {
            int rowSize = baseRowSize + (row < largerRows ? 1 : 0);
            if (index < firstIndex + rowSize) {
                return row;
            }
            firstIndex += rowSize;
        }
        return totalRows - 1;
    }

    @ModifyConstant(method = "generateSpellSlots()V", constant = @Constant(intValue = 15), require = 2)
    private int removeSpellCap(int constant) {
        return Integer.MAX_VALUE;
    }

    @Inject(method = "generateSpellSlots()V", at = @At("RETURN"))
    private void layoutAfterGeneration(CallbackInfo ci) {
        this.sakuratinker_tools$layoutSpellSlots();
    }

    @Inject(method = "renderSpells(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At("HEAD"))
    private void beginSpellViewport(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        this.sakuratinker_tools$layoutSpellSlots();
        int spellAreaX = this.sakuratinker_tools$spellAreaX();
        graphics.enableScissor(this.leftPos + spellAreaX, this.topPos + SPELL_AREA_Y,
            this.leftPos + spellAreaX + SPELL_AREA_WIDTH, this.topPos + SPELL_AREA_Y + SPELL_AREA_HEIGHT);
    }

    @Inject(method = "renderSpells(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At("RETURN"))
    private void endSpellViewport(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        graphics.disableScissor();
        this.sakuratinker_tools$renderScrollbar(graphics);
    }

    @Inject(method = "onSpellBookSlotChanged()V", at = @At("HEAD"))
    private void keepScrollOnSameBook(CallbackInfo ci) {
        if (this.lastSpellBookItem.isEmpty()) {
            this.sakuratinker_tools$scrollRow = 0;
        }
    }

    @Inject(method = "setSelectedIndex(I)V", at = @At("RETURN"))
    private void keepSelectedSpellVisible(int selectedIndex, CallbackInfo ci) {
        if (selectedIndex >= 0 && selectedIndex < this.spellSlots.size()) {
            int selectedRow = sakuratinker_tools$getRowForIndex(selectedIndex, this.spellSlots.size());
            if (selectedRow < this.sakuratinker_tools$scrollRow) {
                this.sakuratinker_tools$scrollRow = selectedRow;
            } else if (selectedRow >= this.sakuratinker_tools$scrollRow + VISIBLE_ROWS) {
                this.sakuratinker_tools$scrollRow = selectedRow - VISIBLE_ROWS + 1;
            }
            this.sakuratinker_tools$clampScroll();
            this.sakuratinker_tools$layoutSpellSlots();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        int horizontalOffset = this.sakuratinker_tools$horizontalLayoutOffset();
        if (scrollDelta != 0.0 && this.sakuratinker_tools$maxScrollRow > 0
            && mouseX >= this.leftPos + SPELL_AREA_X + horizontalOffset
            && mouseX < this.leftPos + SCROLLBAR_X + horizontalOffset + SCROLLBAR_WIDTH
            && mouseY >= this.topPos + SPELL_AREA_Y
            && mouseY < this.topPos + SPELL_AREA_Y + SPELL_AREA_HEIGHT) {
            int previousRow = this.sakuratinker_tools$scrollRow;
            this.sakuratinker_tools$scrollRow += scrollDelta > 0.0 ? -1 : 1;
            this.sakuratinker_tools$clampScroll();
            if (previousRow != this.sakuratinker_tools$scrollRow) {
                this.sakuratinker_tools$layoutSpellSlots();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

    @Unique
    private void sakuratinker_tools$layoutSpellSlots() {
        int spellCount = this.spellSlots.size();
        if (spellCount == 0) {
            this.sakuratinker_tools$scrollRow = 0;
            this.sakuratinker_tools$maxScrollRow = 0;
            return;
        }
        int totalRows = sakuratinker_tools$getTotalRows(spellCount);
        this.sakuratinker_tools$maxScrollRow = Math.max(0, totalRows - VISIBLE_ROWS);
        this.sakuratinker_tools$clampScroll();
        int visibleRowCount = Math.min(totalRows, VISIBLE_ROWS);
        float firstY = -(visibleRowCount * SLOT_SIZE) / 2.0F;
        int baseRowSize = spellCount / totalRows;
        int largerRows = spellCount % totalRows;
        int slotIndex = 0;
        for (int row = 0; row < totalRows; row++) {
            int rowSize = baseRowSize + (row < largerRows ? 1 : 0);
            float firstX = -(rowSize * SLOT_SIZE) / 2.0F;
            boolean visible = row >= this.sakuratinker_tools$scrollRow && row < this.sakuratinker_tools$scrollRow + VISIBLE_ROWS;
            for (int column = 0; column < rowSize; column++) {
                InscriptionSpellSlotAccessor slotInfo = (InscriptionSpellSlotAccessor) this.spellSlots.get(slotIndex++);
                slotInfo.setRelativePosition(
                    new Vec2(firstX + column * SLOT_SIZE, firstY + (row - this.sakuratinker_tools$scrollRow) * SLOT_SIZE));
                Button button = slotInfo.getButton();
                button.visible = visible;
                button.active = visible;
            }
        }
    }

    @Unique
    private void sakuratinker_tools$clampScroll() {
        this.sakuratinker_tools$scrollRow = Math.max(0, Math.min(this.sakuratinker_tools$scrollRow, this.sakuratinker_tools$maxScrollRow));
    }

    @Unique
    private int sakuratinker_tools$spellAreaX() {
        return SPELL_AREA_X + this.sakuratinker_tools$horizontalLayoutOffset();
    }

    @Unique
    private int sakuratinker_tools$horizontalLayoutOffset() {
        int extraWidth = Math.max(0, this.imageWidth - VANILLA_IMAGE_WIDTH);
        return (extraWidth + 1) / 2;
    }

    @Unique
    private void sakuratinker_tools$renderScrollbar(GuiGraphics graphics) {
        if (this.sakuratinker_tools$maxScrollRow > 0) {
            int totalRows = this.sakuratinker_tools$maxScrollRow + VISIBLE_ROWS;
            int trackX = this.leftPos + SCROLLBAR_X + this.sakuratinker_tools$horizontalLayoutOffset();
            int trackY = this.topPos + SPELL_AREA_Y;
            int thumbHeight = Math.max(8, SPELL_AREA_HEIGHT / totalRows);
            int travel = SPELL_AREA_HEIGHT - thumbHeight;
            int thumbY = trackY + Math.round((float) travel * this.sakuratinker_tools$scrollRow / this.sakuratinker_tools$maxScrollRow);
            graphics.fill(trackX, trackY, trackX + SCROLLBAR_WIDTH, trackY + SPELL_AREA_HEIGHT, 1714631475);
            graphics.fill(trackX, thumbY, trackX + SCROLLBAR_WIDTH, thumbY + thumbHeight, -3355444);
        }
    }
}
