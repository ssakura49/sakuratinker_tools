package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker_tools.STTConfig;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager.SelectionOption;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public final class SpellBookStackOverlay {
    private static final float COLLAPSED_STEP = 6.0F;
    private static final float EXPANDED_STEP = 24.0F;
    private static final float SELECTED_SCALE = 1.1F;
    private static final float CARD_SIZE = 22.0F;

    private static final long WHEEL_INSERT_MILLIS = 300L;
    private static final long SCROLL_SHIFT_MILLIS = 180L;
    private static final long SPREAD_HOLD_MILLIS = 620L;
    private static final long SPREAD_OUT_MILLIS = 180L;

    private static final int CARD_Z = 20;
    private static final int INFO_Z = 62;

    private static int lastIndex = Integer.MIN_VALUE;
    private static int previousIndex = Integer.MIN_VALUE;
    private static int lastSpellCount;
    private static int transitionDirection = 1;
    private static SpellCardAnimation.SelectionSource transitionSource = SpellCardAnimation.SelectionSource.UNKNOWN;
    private static long transitionStartedAt = Long.MIN_VALUE;
    private static float verticalOffsetStart;
    private static long spreadStartedAt = Long.MIN_VALUE;
    private static long lastScrollChangedAt = Long.MIN_VALUE;
    private static boolean persistentExpanded;

    private SpellBookStackOverlay() {
    }

    public static boolean isModLoaded() {
        return ModList.get().isLoaded("irons_spellbooks");
    }

    public static void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (minecraft.options.hideGui || player == null || player.isSpectator()) {
            return;
        }
        if (!STTConfig.COMPAT.ENABLE_ISS_COMPAT_UI.get())return;
        SpellSelectionManager manager = ClientMagicData.getSpellSelectionManager();
        List<SelectionOption> options = manager.getAllSpells();
        int spellCount = options.size();
        if (spellCount <= 0) {
            reset();
            return;
        }
        int selectionIndex = Mth.clamp(manager.getGlobalSelectionIndex(), 0, spellCount - 1);
        long now = Util.getMillis();
        updateSelection(selectionIndex, spellCount, now);

        int x = 13;
        float mainY = screenHeight * 0.5F - CARD_SIZE / 2.0F;
        float spread = persistentExpanded ? 1.0F : spreadProgress(now);
        SpellCardAnimation.CastingTransform castingTransform =
            SpellCardAnimation.castingTransform(ClientMagicData.isCasting());
        boolean transitionActive =
            previousIndex != Integer.MIN_VALUE && now - transitionStartedAt < transitionDuration();

        graphics.enableScissor(0, 0, screenWidth, screenHeight);
        try {
            if (transitionActive && transitionSource == SpellCardAnimation.SelectionSource.WHEEL) {
                renderWheelDeck(graphics, options, selectionIndex, spellCount, x, mainY, castingTransform, transitionActive, now);
            } else {
                renderVerticalDeck(graphics, options, selectionIndex, spellCount, x, mainY, spread, castingTransform, transitionActive, screenHeight, now);
            }
            if (!transitionActive) {
                previousIndex = Integer.MIN_VALUE;
                transitionSource = SpellCardAnimation.SelectionSource.UNKNOWN;
                verticalOffsetStart = 0.0F;
            }
        } finally {
            graphics.disableScissor();
        }

        drawPersistentInfo(graphics, x, mainY, options.get(selectionIndex).spellData, player, selectionIndex, spellCount);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void updateSelection(int selectionIndex, int spellCount, long now) {
        if (lastIndex == Integer.MIN_VALUE || (selectionIndex == lastIndex && spellCount == lastSpellCount)) {
            if (lastIndex == Integer.MIN_VALUE) {
                lastIndex = selectionIndex;
                lastSpellCount = spellCount;
            }
            return;
        }
        if (selectionIndex == lastIndex) {
            lastSpellCount = spellCount;
            return;
        }
        int oldIndex = Math.floorMod(lastIndex, spellCount);
        float retainedOffset = currentVerticalOffset(now);
        SpellCardAnimation.SelectionTransition transition =
            SpellCardAnimation.consumeSelectionTransition(oldIndex, selectionIndex, spellCount);
        transitionSource = transition.source();
        transitionDirection = transition.direction();
        previousIndex = oldIndex;
        lastIndex = selectionIndex;
        lastSpellCount = spellCount;
        transitionStartedAt = now;
        if (transitionSource != SpellCardAnimation.SelectionSource.WHEEL) {
            verticalOffsetStart = retainedOffset + signedDistance(selectionIndex, oldIndex, spellCount);
            spreadStartedAt = now;
            lastScrollChangedAt = now;
        } else {
            verticalOffsetStart = 0.0F;
            spreadStartedAt = Long.MIN_VALUE;
            lastScrollChangedAt = Long.MIN_VALUE;
        }
    }

    private static void renderVerticalDeck(GuiGraphics graphics, List<SelectionOption> options,
                                           int selectionIndex, int spellCount, int x, float mainY, float spread,
                                           SpellCardAnimation.CastingTransform castingTransform, boolean transitionActive,
                                           int screenHeight, long now) {
        float offset = currentVerticalOffset(now);
        float transitionProgress = transitionActive ? transitionProgress(now) : 1.0F;
        int maximumDistance = spellCount / 2;
        for (int distance = maximumDistance; distance >= 1; distance--) {
            int lowerIndex = Math.floorMod(selectionIndex + distance, spellCount);
            int upperIndex = Math.floorMod(selectionIndex - distance, spellCount);
            drawPositionedCard(graphics, options.get(lowerIndex), lowerIndex, selectionIndex, spellCount,
                x, mainY, spread, offset, screenHeight, false, castingTransform, false, transitionProgress);
            if (upperIndex != lowerIndex) {
                drawPositionedCard(graphics, options.get(upperIndex), upperIndex, selectionIndex, spellCount,
                    x, mainY, spread, offset, screenHeight, false, castingTransform, false, transitionProgress);
            }
        }
        drawPositionedCard(graphics, options.get(selectionIndex), selectionIndex, selectionIndex, spellCount,
            x, mainY, spread, offset, screenHeight, true, castingTransform, transitionActive, transitionProgress);
    }

    private static void drawPositionedCard(GuiGraphics graphics, SelectionOption option, int index,
                                           int selectedIndex, int spellCount, int x, float mainY, float spread, float offset, int screenHeight,
                                           boolean selected, SpellCardAnimation.CastingTransform castingTransform, boolean transitionActive,
                                           float transitionProgress) {
        float relative = signedDistance(index, selectedIndex, spellCount) + offset;
        float y = cardY(mainY, relative, EXPANDED_STEP, spread);
        if (y + CARD_SIZE < 0.0F || y > screenHeight) {
            return;
        }
        float opacity = cardOpacity(relative, spread) * Mth.lerp(spread, 1.0F, verticalFade(y, screenHeight));
        if (opacity <= 0.001F) {
            return;
        }
        float centerProximity = 1.0F - Mth.clamp(Math.abs(y - mainY) / EXPANDED_STEP, 0.0F, 1.0F);
        float emphasis = selected ? spread * centerProximity : 0.0F;
        float scale = Mth.lerp(emphasis, 1.0F, SELECTED_SCALE);
        if (selected && emphasis > 0.001F) {
            drawSelectionBackdrop(graphics, x, y, emphasis, opacity);
        }
        drawSpellCard(graphics, option, x, y, scale, opacity, selected, selected ? 42.0F : 20.0F);
        if (selected) {
            drawCooldown(graphics, option, x, y, scale, opacity);
            drawSelectedFrame(graphics, x, y, scale, opacity, castingTransform);
            if (transitionActive) {
                drawDirectionArrow(graphics, x, y, transitionDirection, transitionProgress, opacity);
            }
        }
    }

    private static void renderWheelDeck(GuiGraphics graphics, List<SelectionOption> options,
                                        int selectionIndex, int spellCount, int x, float mainY,
                                        SpellCardAnimation.CastingTransform castingTransform, boolean transitionActive, long now) {
        int oldIndex = Math.floorMod(previousIndex, spellCount);
        float rawProgress = transitionProgress(now);
        float progress = smootherStep(rawProgress);

        for (int index = 0; index < spellCount; index++) {
            if (index == selectionIndex || index == oldIndex) {
                continue;
            }
            int oldRelative = signedDistance(index, oldIndex, spellCount);
            int newRelative = signedDistance(index, selectionIndex, spellCount);
            float opacity = Mth.lerp(progress, cardOpacity(oldRelative, 0.0F), cardOpacity(newRelative, 0.0F));
            if (opacity > 0.001F) {
                drawSpellCard(graphics, options.get(index), x,
                    Mth.lerp(progress, cardY(mainY, oldRelative, 0.0F, 0.0F), cardY(mainY, newRelative, 0.0F, 0.0F)),
                    1.0F, opacity, false, 20.0F);
            }
        }

        int oldRelative = signedDistance(oldIndex, selectionIndex, spellCount);
        float oldTargetY = cardY(mainY, oldRelative, 0.0F, 0.0F);
        float oldEndOpacity = Math.abs(oldRelative) <= 2 ? 1.0F : 0.0F;
        float retreat = Mth.sin(progress * (float) Math.PI);
        drawSpellCard(graphics, options.get(oldIndex), x - retreat * 8.0F,
            Mth.lerp(progress, mainY, oldTargetY), 1.0F, Mth.lerp(progress, 1.0F, oldEndOpacity), false, 34.0F);

        int incomingRelative = signedDistance(selectionIndex, oldIndex, spellCount);
        float incomingStartY = cardY(mainY, incomingRelative, 0.0F, 0.0F);
        float pull = smootherStep(rawProgress / 0.38F);
        float insert = smootherStep((rawProgress - 0.38F) / 0.62F);
        float pulledX = Mth.lerp(pull, x - 6.0F, x + 16.0F);
        float pulledY = Mth.lerp(pull, incomingStartY, mainY + transitionDirection * 4.0F);
        float selectedX = Mth.lerp(insert, pulledX, x);
        float selectedY = Mth.lerp(insert, pulledY, mainY);
        float selectedScale = Mth.lerp(insert, 0.96F, 1.0F);
        SelectionOption selectedOption = options.get(selectionIndex);
        drawSpellCard(graphics, selectedOption, selectedX, selectedY, selectedScale, 1.0F, true, 46.0F);
        drawCooldown(graphics, selectedOption, selectedX, selectedY, selectedScale, 1.0F);
        drawSelectedFrame(graphics, selectedX, selectedY, selectedScale, 1.0F, castingTransform);
        if (transitionActive) {
            drawDirectionArrow(graphics, selectedX, selectedY, transitionDirection, rawProgress, 1.0F);
        }
    }

    private static void drawSpellCard(GuiGraphics graphics, SelectionOption option, float x, float y,
            float scale, float opacity, boolean selected, float z) {
        SpellData spellData = option.spellData;
        if (spellData == null || spellData == SpellData.EMPTY || opacity <= 0.001F) {
            return;
        }
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + CARD_SIZE / 2.0F, y + CARD_SIZE / 2.0F, z);
        pose.scale(scale, scale, 1.0F);
        pose.translate(-CARD_SIZE / 2.0F, -CARD_SIZE / 2.0F, 0.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        graphics.blit(SpellBarOverlay.TEXTURE, 0, 0, 66, 84, 22, 22);
        graphics.blit(spellData.getSpell().getSpellIconResource(), 3, 3, 0.0F, 0.0F, 16, 16, 16, 16);
        graphics.flush();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        pose.popPose();
    }

    private static void drawSelectionBackdrop(GuiGraphics graphics, float x, float y, float emphasis, float opacity) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + CARD_SIZE / 2.0F, y + CARD_SIZE / 2.0F, 18.0F);
        float backdropScale = Mth.lerp(emphasis, 0.92F, 1.0F);
        pose.scale(backdropScale, backdropScale, 1.0F);
        int outerAlpha = Mth.clamp(Math.round(138.0F * emphasis * opacity), 0, 255);
        int innerAlpha = Mth.clamp(Math.round(220.0F * emphasis * opacity), 0, 255);
        graphics.fill(-14, -14, 14, 14, outerAlpha << 24 | 5583724);
        graphics.fill(-12, -12, 12, 12, innerAlpha << 24 | 591375);
        pose.popPose();
    }

    private static void drawCooldown(GuiGraphics graphics, SelectionOption option, float x, float y,
            float cardScale, float opacity) {
        SpellData spellData = option.spellData;
        if (spellData == null || spellData == SpellData.EMPTY) {
            return;
        }
        float cooldown = Mth.clamp(ClientMagicData.getCooldownPercent(spellData.getSpell()), 0.0F, 1.0F);
        if (cooldown <= 0.0F) {
            return;
        }
        int cooldownHeight = Mth.floor(16.0F * cooldown);
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + CARD_SIZE / 2.0F, y + CARD_SIZE / 2.0F, 54.0F);
        pose.scale(cardScale, cardScale, 1.0F);
        pose.translate(-CARD_SIZE / 2.0F, -CARD_SIZE / 2.0F, 0.0F);
        int alpha = Mth.clamp(Math.round(217.0F * opacity), 0, 255);
        graphics.fill(3, 19 - cooldownHeight, 19, 19, alpha << 24 | 460050);
        pose.popPose();
    }

    private static void drawSelectedFrame(GuiGraphics graphics, float x, float y, float cardScale,
            float opacity, SpellCardAnimation.CastingTransform transform) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + CARD_SIZE / 2.0F, y + CARD_SIZE / 2.0F, 60.0F);
        float frameScale = cardScale * transform.scale();
        pose.scale(frameScale, frameScale, 1.0F);
        pose.mulPose(Axis.ZP.rotationDegrees(transform.rotationDegrees()));
        pose.translate(-CARD_SIZE / 2.0F, -CARD_SIZE / 2.0F, 0.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        graphics.blit(SpellBarOverlay.TEXTURE, 0, 0, transform.active() ? 88 : 0, 84, 22, 22);
        graphics.flush();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        pose.popPose();
    }

    private static void drawDirectionArrow(GuiGraphics graphics, float x, float y, int direction,
            float progress, float opacity) {
        Font font = Minecraft.getInstance().font;
        Component arrow = Component.literal(direction < 0 ? "▲" : "▼");
        float pulse = Mth.sin(Mth.clamp(progress, 0.0F, 1.0F) * (float) Math.PI);
        int alpha = Mth.clamp(Math.round((0.42F + pulse * 0.58F) * opacity * 255.0F), 0, 255);
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + CARD_SIZE / 2.0F + 4.0F + 2.0F, y + CARD_SIZE / 2.0F - 4.5F, INFO_Z);
        pose.scale(0.78F + pulse * 0.14F, 0.78F + pulse * 0.14F, 1.0F);
        graphics.drawString(font, arrow, 0, 0, alpha << 24 | 15120383, true);
        pose.popPose();
    }

    private static void drawPersistentInfo(GuiGraphics graphics, int x, float mainY, SpellData spellData,
            LocalPlayer player, int selectionIndex, int spellCount) {
        if (spellData == null || spellData == SpellData.EMPTY) {
            return;
        }
        AbstractSpell spell = spellData.getSpell();
        int level = spell.getLevelFor(spellData.getLevel(), player);
        int maximumLevel = spell.getMaxLevel();
        int manaCost = spell.getManaCost(level);
        int castTime = spell.getEffectiveCastTime(level, player);
        int recastCount = spell.getRecastCount(level, player);
        float spellPower = spell.getSpellPower(level, player);
        CooldownInstance activeCooldown = ClientMagicData.getCooldowns().getSpellCooldowns().get(spell.getSpellId());
        int totalCooldown = activeCooldown == null ? spell.getSpellCooldown() : activeCooldown.getSpellCooldown();
        int remainingCooldown = activeCooldown == null ? 0 : Math.max(0, activeCooldown.getCooldownRemaining());

        List<Component> lines = new ArrayList<>();
        lines.add(spell.getDisplayName(player));
        lines.add(Component.literal((selectionIndex + 1) + " / " + spellCount));
        lines.add(Component.translatable("gui.sakuratinker.level", level, maximumLevel));
        lines.add(Component.translatable("gui.sakuratinker.rarity", spell.getRarity(level).getDisplayName()));
        lines.add(Component.translatable("gui.sakuratinker.mana", manaCost));
        lines.add(Component.translatable("gui.sakuratinker.cooldown", seconds(remainingCooldown), seconds(totalCooldown)));
        lines.add(Component.translatable("gui.sakuratinker.cast_time", seconds(castTime)));
        lines.add(Component.translatable("gui.sakuratinker.spell_power", String.format(Locale.ROOT, "%.2f", spellPower)));
        lines.add(Component.translatable("gui.sakuratinker.recast", recastCount));
        lines.addAll(spell.getUniqueInfo(level, player));

        Font font = Minecraft.getInstance().font;
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + CARD_SIZE / 2.0F + 13.0F, mainY - 4.0F, 55.0F);
        pose.scale(0.72F, 0.72F, 1.0F);
        for (int i = 0; i < lines.size(); i++) {
            int color = i == 0 ? -723724 : -3618616;
            graphics.drawString(font, lines.get(i), 0, i * 10, color, true);
        }
        pose.popPose();
    }

    private static String seconds(int ticks) {
        return String.format(Locale.ROOT, "%.2f", Math.max(0, ticks) / 20.0F);
    }

    private static float cardY(float mainY, float relative, float expandedStep, float spread) {
        float collapsedRelative = Mth.clamp(relative, -2.0F, 2.0F);
        float collapsedY = mainY + collapsedRelative * COLLAPSED_STEP;
        float expandedY = mainY + relative * expandedStep;
        return Mth.lerp(spread, collapsedY, expandedY);
    }

    private static float cardOpacity(float relative, float spread) {
        float collapsedOpacity = Math.abs(relative) <= 2.001F ? 1.0F : 0.0F;
        return Mth.lerp(spread, collapsedOpacity, 1.0F);
    }

    private static float verticalFade(float y, int screenHeight) {
        float centerY = y + CARD_SIZE / 2.0F;
        float halfHeight = Math.max(1.0F, screenHeight * 0.5F - CARD_SIZE / 2.0F);
        float distance = Math.abs(centerY - screenHeight * 0.5F) / halfHeight;
        return 1.0F - smootherStep(distance);
    }

    public static void playSelectionSound(int previousIndex, int selectedIndex, int spellCount) {
        if (spellCount > 1 && previousIndex != selectedIndex) {
            playDialSound(signedDistance(selectedIndex, previousIndex, spellCount) < 0 ? -1 : 1);
        }
    }

    private static void playDialSound(int direction) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        float variation = (player.getRandom().nextFloat() - 0.5F) * 0.1F;
        float directionPitch = direction < 0 ? 0.04F : -0.02F;
        float pitch = 1.32F + directionPitch + variation;
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), pitch, 0.42F));
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_HAT.value(), pitch * 1.08F, 0.14F));
    }

    private static int signedDistance(int index, int center, int count) {
        int distance = index - center;
        int half = count / 2;
        if (distance > half) {
            distance -= count;
        } else if (distance < -half) {
            distance += count;
        }
        return distance;
    }

    private static float spreadProgress(long now) {
        if (spreadStartedAt != Long.MIN_VALUE && lastScrollChangedAt != Long.MIN_VALUE) {
            long idle = now - lastScrollChangedAt;
            float close = idle <= SPREAD_HOLD_MILLIS ? 1.0F
                : 1.0F - smootherStep((float) (idle - SPREAD_HOLD_MILLIS) / (float) SPREAD_OUT_MILLIS);
            return Mth.clamp(close, 0.0F, 1.0F);
        }
        return 0.0F;
    }

    private static long transitionDuration() {
        return transitionSource == SpellCardAnimation.SelectionSource.WHEEL
            ? WHEEL_INSERT_MILLIS
            : SCROLL_SHIFT_MILLIS;
    }

    private static float transitionProgress(long now) {
        return Mth.clamp((float) (now - transitionStartedAt) / (float) transitionDuration(), 0.0F, 1.0F);
    }

    private static float currentVerticalOffset(long now) {
        return previousIndex != Integer.MIN_VALUE
            && transitionSource != SpellCardAnimation.SelectionSource.WHEEL
            && transitionStartedAt != Long.MIN_VALUE
            ? verticalOffsetStart * (1.0F - smootherStep(transitionProgress(now)))
            : 0.0F;
    }

    public static void toggleExpandedMode() {
        persistentExpanded = !persistentExpanded;
    }

    public static boolean isPersistentExpanded() {
        return persistentExpanded;
    }

    private static float smootherStep(float value) {
        float clamped = Mth.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * clamped * (clamped * (clamped * 6.0F - 15.0F) + 10.0F);
    }

    private static void reset() {
        lastIndex = Integer.MIN_VALUE;
        previousIndex = Integer.MIN_VALUE;
        lastSpellCount = 0;
        transitionSource = SpellCardAnimation.SelectionSource.UNKNOWN;
        transitionStartedAt = Long.MIN_VALUE;
        verticalOffsetStart = 0.0F;
        spreadStartedAt = Long.MIN_VALUE;
        lastScrollChangedAt = Long.MIN_VALUE;
        SpellCardAnimation.reset();
    }
}
