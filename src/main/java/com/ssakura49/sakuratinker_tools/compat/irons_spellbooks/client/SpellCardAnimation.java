package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.client;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public final class SpellCardAnimation {
    private static final long SELECTION_TTL_MS = 1000L;
    private static final long MIN_CAST_ANIMATION_MS = 420L;
    private static final long CAST_ROTATION_PERIOD_MS = 1400L;
    private static final float CAST_ENTRANCE_MS = 150.0F;
    private static final float CAST_PULSE_SPEED = 0.012F;
    private static final float CAST_ENTRANCE_SCALE = 0.085F;
    private static final float CAST_PULSE_SCALE = 0.032F;
    private static final float ROTATION_DEGREES_PER_MS = 360.0F / CAST_ROTATION_PERIOD_MS;

    private static SelectionSource pendingSource = SelectionSource.UNKNOWN;
    private static int pendingDirection;
    private static int pendingPreviousIndex = Integer.MIN_VALUE;
    private static int pendingSelectedIndex = Integer.MIN_VALUE;
    private static long pendingStartedAt = Long.MIN_VALUE;
    private static long pendingExpiresAt = Long.MIN_VALUE;

    private static long castingStartedAt = Long.MIN_VALUE;
    private static long castingMinimumEndAt = Long.MIN_VALUE;
    private static boolean castingSignaled;

    private SpellCardAnimation() {
    }

    public static void signalWheelSelection(int previousIndex, int selectedIndex, int spellCount) {
        signalSelection(SelectionSource.WHEEL, previousIndex, selectedIndex, spellCount);
    }

    public static void signalScrollSelection(int previousIndex, int selectedIndex, int spellCount) {
        signalSelection(SelectionSource.SCROLL, previousIndex, selectedIndex, spellCount);
    }

    public static void signalCastingStarted() {
        long now = Util.getMillis();
        if (castingStartedAt == Long.MIN_VALUE) {
            castingStartedAt = now;
        }
        castingSignaled = true;
        castingMinimumEndAt = Math.max(castingMinimumEndAt, now + MIN_CAST_ANIMATION_MS);
    }

    public static void signalCastingStopped() {
        castingSignaled = false;
    }


    public static SelectionTransition consumeSelectionTransition(int previousIndex, int selectedIndex, int spellCount) {
        long now = Util.getMillis();
        boolean matchingIndices = pendingPreviousIndex == Integer.MIN_VALUE
            || (pendingPreviousIndex == previousIndex && pendingSelectedIndex == selectedIndex);
        SelectionSource source = now <= pendingExpiresAt && matchingIndices
            ? pendingSource
            : SelectionSource.UNKNOWN;
        int direction = pendingDirection;
        if (direction == 0) {
            direction = signedDistance(selectedIndex, previousIndex, spellCount) < 0 ? -1 : 1;
        }
        long startedAt = source != SelectionSource.UNKNOWN && pendingStartedAt != Long.MIN_VALUE
            ? pendingStartedAt
            : now;
        clearPendingSelection();
        return new SelectionTransition(source, direction, startedAt);
    }


    public static CastingTransform castingTransform(boolean castingHint) {
        long now = Util.getMillis();
        boolean active = castingHint || castingSignaled || now < castingMinimumEndAt;
        if (!active) {
            castingStartedAt = Long.MIN_VALUE;
            castingMinimumEndAt = Long.MIN_VALUE;
            return CastingTransform.IDENTITY;
        }
        if (castingStartedAt == Long.MIN_VALUE) {
            castingStartedAt = now;
            castingMinimumEndAt = Math.max(castingMinimumEndAt, now + MIN_CAST_ANIMATION_MS);
        }
        long elapsed = Math.max(0L, now - castingStartedAt);
        float entrance = smootherStep(elapsed / CAST_ENTRANCE_MS);
        float pulse = 0.5F + 0.5F * Mth.sin((float) elapsed * CAST_PULSE_SPEED);
        float scale = 1.0F + entrance * (CAST_ENTRANCE_SCALE + pulse * CAST_PULSE_SCALE);
        float rotation = (float) (elapsed % CAST_ROTATION_PERIOD_MS) * ROTATION_DEGREES_PER_MS;
        return new CastingTransform(true, scale, rotation);
    }

    public static void reset() {
        pendingSource = SelectionSource.UNKNOWN;
        pendingDirection = 0;
        pendingPreviousIndex = Integer.MIN_VALUE;
        pendingSelectedIndex = Integer.MIN_VALUE;
        pendingStartedAt = Long.MIN_VALUE;
        pendingExpiresAt = Long.MIN_VALUE;
        castingStartedAt = Long.MIN_VALUE;
        castingMinimumEndAt = Long.MIN_VALUE;
        castingSignaled = false;
    }

    private static void signalSelection(SelectionSource source, int previousIndex, int selectedIndex, int spellCount) {
        pendingSource = source;
        pendingPreviousIndex = previousIndex;
        pendingSelectedIndex = selectedIndex;
        pendingDirection = signedDistance(selectedIndex, previousIndex, spellCount) < 0 ? -1 : 1;
        pendingStartedAt = Util.getMillis();
        pendingExpiresAt = pendingStartedAt + SELECTION_TTL_MS;
    }

    private static void clearPendingSelection() {
        pendingSource = SelectionSource.UNKNOWN;
        pendingDirection = 0;
        pendingPreviousIndex = Integer.MIN_VALUE;
        pendingSelectedIndex = Integer.MIN_VALUE;
        pendingStartedAt = Long.MIN_VALUE;
        pendingExpiresAt = Long.MIN_VALUE;
    }

    private static int signedDistance(int index, int center, int count) {
        if (count <= 0) {
            return 0;
        }
        int distance = index - center;
        int half = count / 2;
        if (distance > half) {
            distance -= count;
        } else if (distance < -half) {
            distance += count;
        }
        return distance;
    }

    /** smoothstep 6x^5-15x^4+10x^3 on [0,1]. */
    private static float smootherStep(float value) {
        float clamped = Mth.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * clamped * (clamped * (clamped * 6.0F - 15.0F) + 10.0F);
    }

    public enum SelectionSource {
        UNKNOWN, SCROLL, WHEEL
    }

    public record SelectionTransition(SelectionSource source, int direction, long startedAt) {
    }

    public record CastingTransform(boolean active, float scale, float rotationDegrees) {
        public static final CastingTransform IDENTITY = new CastingTransform(false, 1.0F, 0.0F);
    }
}
