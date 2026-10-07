package com.ssakura49.sakuratinker_tools.client.recoil;

import com.ssakura49.sakuratinker_tools.STTConfig;
import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import com.ssakura49.sakuratinker_tools.init.STTKeys;
import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableGunItem;
import com.ssakura49.sakuratinker_tools.network.PacketHandler;
import com.ssakura49.sakuratinker_tools.network.s2c.ReloadPacket;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(
        modid = SakuraTinkerTools.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = {Dist.CLIENT}
)
public class RecoilHandler {
    public static RecoilHandler INSTANCE;
    private float recoilPitch;
    private float pendingRecoilPitch;
    @Getter
    private long lastRecoilTime;
    private static final float RECOIL_PITCH_PER_SHOT = 3.25F;
    private static final float MANUAL_COMPENSATION_THRESHOLD = 0.05F;
    private static final Map<Player, RecoilHandler> PLAYER_RECOIL_HANDLERS = new WeakHashMap<>();
    private float recoilYaw;
    private float prevXRot;
    private float prevYRot;
    private float pendingRecoilYaw;
    private static final float RECOIL_YAW_RANDOM = 0.25F;
    private static final float MAX_RECOIL_PITCH = 30.0F;
    private static final float MAX_RECOIL_YAW = 20.0F;
    private int shotsFired = 0;
    private long lastShotTime = 0L;

    public RecoilHandler(Player player) {
        this.recoilPitch = 0.0F;
        this.recoilYaw = 0.0F;
        this.prevXRot = player.getXRot();
        this.prevYRot = player.getYRot();
    }

    public static RecoilHandler getHandler(Player player) {
        return PLAYER_RECOIL_HANDLERS.computeIfAbsent(player, RecoilHandler::new);
    }

    public static void removeHandler(Player player) {
        PLAYER_RECOIL_HANDLERS.remove(player);
    }

    public void applyRecoil(Player player, float recoilAmount) {
        if (player == Minecraft.getInstance().player) {
            long now = System.currentTimeMillis();
            if (now - this.lastShotTime > 200L) {
                this.shotsFired = 0;
            }

            this.shotsFired++;
            this.lastShotTime = now;
            this.lastRecoilTime = now;
            float curveFactor = 1.0F + 0.07F * (float)this.shotsFired;
            if (this.shotsFired == 1) {
                curveFactor *= 0.8F;
            }

            float yawOffset = (player.getRandom().nextFloat() - 0.5F) * 2.0F * 0.25F;
            float verticalScale = (float) Math.max(0.0F, STTConfig.COMMON.REVOLVER_VERTICAL_RECOIL_SCALE.get());
            float horizontalScale = (float) Math.max(0.0F, STTConfig.COMMON.REVOLVER_HORIZONTAL_RECOIL_SCALE.get());
            this.pendingRecoilPitch += recoilAmount * verticalScale * curveFactor;
            this.pendingRecoilYaw += yawOffset * horizontalScale * curveFactor;
        }
    }

    public void applyRecoil(Player player) {
        this.applyRecoil(player, RECOIL_PITCH_PER_SHOT);
    }

    public void tick(Player player) {
        if (player == Minecraft.getInstance().player) {
            float currentX = player.getXRot();
            float currentY = player.getYRot();
            float deltaX = currentX - this.prevXRot;
            float deltaY = Mth.wrapDegrees(currentY - this.prevYRot);
            boolean isManualCompensating = this.applyManualCompensation(deltaX, deltaY);
            this.applyPendingRecoil(player);
            if (player.isUsingItem() && player.getUseItem().getItem() instanceof ModifiableGunItem) {
                this.lastRecoilTime = System.currentTimeMillis();
            }

            boolean isFiring = System.currentTimeMillis() - this.lastRecoilTime < 150L;
            if (!isManualCompensating) {
                this.recoverAxis(player, true, isFiring);
                this.recoverAxis(player, false, isFiring);
            }

            this.prevXRot = player.getXRot();
            this.prevYRot = player.getYRot();
        }
    }

    private boolean applyManualCompensation(float deltaPitch, float deltaYaw) {
        boolean compensated = false;
        if (this.recoilPitch > 0.0F && deltaPitch > 0.05F) {
            float compensate = Math.min(this.recoilPitch, deltaPitch * 0.8F);
            this.recoilPitch -= compensate;
            compensated = true;
        }

        if (this.recoilYaw != 0.0F) {
            float compensationDir = -Math.signum(this.recoilYaw);
            if (Math.signum(deltaYaw) == compensationDir) {
                float compensateAmount = Math.abs(deltaYaw) * 0.7F;
                this.recoilYaw += compensationDir * compensateAmount;
                if (Math.abs(this.recoilYaw) < 0.05F || Math.signum(this.recoilYaw) != Math.signum(this.recoilYaw + compensationDir * 10.0F)) {
                    this.recoilYaw = 0.0F;
                }

                compensated = true;
            }
        }

        return compensated;
    }

    private void applyPendingRecoil(Player player) {
        if (!(Math.abs(this.pendingRecoilPitch) < 0.001F) || !(Math.abs(this.pendingRecoilYaw) < 0.001F)) {
            float smoothPitch = 0.1F;
            float smoothYaw = 0.08F;
            float applyPitch = this.pendingRecoilPitch * smoothPitch;
            float applyYaw = this.pendingRecoilYaw * smoothYaw;
            this.recoilPitch += applyPitch;
            this.recoilYaw += applyYaw;
            this.recoilPitch = Mth.clamp(this.recoilPitch, -30.0F, 30.0F);
            this.recoilYaw = Mth.clamp(this.recoilYaw, -20.0F, 20.0F);
            if (STTConfig.COMMON.ENABLE_REVOLVER_RECOIL.get()) {
                player.setXRot(player.getXRot() - applyPitch);
                player.setYRot(player.getYRot() + applyYaw);
            }

            this.pendingRecoilPitch -= applyPitch;
            this.pendingRecoilYaw -= applyYaw;
        }
    }

    private void recoverAxis(Player player, boolean pitch, boolean isFiring) {
        if (!isFiring) {
            return;
        }
        float value = pitch ? this.recoilPitch : this.recoilYaw;
        if (!(Math.abs(value) < 0.001F)) {
            float rate = pitch ? (isFiring ? 0.009F : 0.035F) : (isFiring ? 0.013F : 0.055F);
            float recover = Math.abs(value) * rate;
            if (pitch) {
                this.recoilPitch = this.recoilPitch - Math.signum(this.recoilPitch) * recover;
                if (STTConfig.COMMON.ENABLE_REVOLVER_RECOIL.get()) {
                    player.setXRot(player.getXRot() + recover * Math.signum(this.recoilPitch));
                }
            } else {
                this.recoilYaw = this.recoilYaw - Math.signum(this.recoilYaw) * recover;
                if (STTConfig.COMMON.ENABLE_REVOLVER_RECOIL.get()) {
                    player.setYRot(player.getYRot() - Math.signum(this.recoilYaw) * recover);
                }
            }
        }
    }

    public void reset(Player player) {
        if (STTConfig.COMMON.ENABLE_REVOLVER_RECOIL.get()) {
            player.setXRot(player.getXRot() + this.recoilPitch);
            player.setYRot(player.getYRot() - this.recoilYaw);
        }

        this.recoilPitch = 0.0F;
        this.recoilYaw = 0.0F;
        this.pendingRecoilPitch = 0.0F;
        this.pendingRecoilYaw = 0.0F;
        this.prevXRot = player.getXRot();
        this.prevYRot = player.getYRot();
    }

    private static Player lastPlayer;
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !mc.isPaused()) {
                if (mc.player != lastPlayer) {
                    lastPlayer = mc.player;
                    INSTANCE = RecoilHandler.getHandler(mc.player);
                }

                if (INSTANCE != null) {
                    INSTANCE.tick(mc.player);
                }

                while (STTKeys.getReloadKey().consumeClick()) {
                    PacketHandler.INSTANCE.sendToServer(new ReloadPacket());
                }
            }
        }
    }
}
