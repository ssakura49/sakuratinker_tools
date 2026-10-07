package com.ssakura49.sakuratinker_tools.client.hud;

import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import com.ssakura49.sakuratinker_tools.content.capability.ToolBulletSlotCapability;
import com.ssakura49.sakuratinker_tools.content.tool.item.RevolverItem;
import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableBulletItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

@Mod.EventBusSubscriber(modid = SakuraTinkerTools.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT})
public class RevolverAmmoHud {
    private static final int CELL_SIZE = 10;
    private static final int CELL_GAP = 4;
    private static final int MAX_REFRESH_GAP = 5;

    private static ItemStack lastGun = ItemStack.EMPTY;
    private static int lastTick = -1;
    private static int slotCount = 0;
    private static int currentChamber = 0;
    /** 每格剩余比例 */
    private static float[] shots = new float[0];
    private static boolean[] fired = new boolean[0];

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        ItemStack gun = player.getMainHandItem();
        if (!(gun.getItem() instanceof RevolverItem)) {
            gun = player.getOffhandItem();
            if (!(gun.getItem() instanceof RevolverItem)) return;
        }

        int tick = player.tickCount;
        boolean needsRefresh = gun != lastGun || (tick - lastTick) >= MAX_REFRESH_GAP;
        if (needsRefresh) {
            refresh(gun);
            lastGun = gun;
            lastTick = tick;
        }
        if (slotCount <= 0) return;

        GuiGraphics gui = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int totalWidth = slotCount * CELL_SIZE + (slotCount - 1) * CELL_GAP;
        int rowX = (screenWidth - totalWidth) / 2;
        int rowY = screenHeight - 42;

        for (int i = 0; i < slotCount; i++) {
            int x = rowX + i * (CELL_SIZE + CELL_GAP);
            boolean current = i == currentChamber;
            float ratio = shots[i];


            if (current) {
                gui.fill(x - 1, rowY - 1, x + CELL_SIZE + 1, rowY + CELL_SIZE + 1, 0xFFFFC91A);
            }
            gui.fill(x, rowY, x + CELL_SIZE, rowY + CELL_SIZE, 0xFF3A3A3A);
            if (ratio > 0.0F) {
                int fillH = (int) Math.ceil(ratio * CELL_SIZE);
                fillH = Math.min(fillH, CELL_SIZE);
                int fillColor = fired[i]
                        ? (current ? 0xFFFF6B6B : 0xFFE2554D)
                        : (current ? 0xFFFFE15A : 0xFFC8A24B);
                gui.fill(x, rowY + CELL_SIZE - fillH, x + CELL_SIZE, rowY + CELL_SIZE, fillColor);
            }
        }
    }

    private static void refresh(ItemStack gun) {
        ToolBulletSlotCapability capability = gun.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .filter(ToolBulletSlotCapability.class::isInstance)
                .map(ToolBulletSlotCapability.class::cast)
                .orElse(null);
        if (capability == null) {
            slotCount = 0;
            shots = new float[0];
            currentChamber = 0;
            return;
        }
        slotCount = capability.getSlots();
        shots = new float[slotCount];
        ToolStack tool = ToolStack.from(gun);
        int rawChamber = Math.floorMod(tool.getPersistentData().getInt(ToolBulletSlotCapability.KEY_CURRENT_CHAMBER), slotCount);
        for (int i = 0; i < slotCount; i++) {
            ItemStack bullet = capability.getStackInSlot(i);
            if (bullet.isEmpty()) {
                shots[i] = -1.0F;
            } else if (bullet.getItem() instanceof ModifiableBulletItem) {
                ToolStack bulletTool = ToolStack.from(bullet);
                if (bulletTool.isBroken()) {
                    shots[i] = -1.0F;
                } else {
                    float cur = (float) bulletTool.getCurrentDurability();
                    float max = Math.max(1.0F, bulletTool.getStats().get(ToolStats.DURABILITY));
                    shots[i] = Math.min(1.0F, cur / max);
                }
            } else {
                int stackSize = Math.max(1, bullet.getMaxStackSize());
                shots[i] = Math.min(1.0F, bullet.getCount() / (float) stackSize);
            }
        }
        Level level = Minecraft.getInstance().level;
        boolean reloading = false;
        if (level != null && tool.getPersistentData().contains(RevolverItem.KEY_RELOAD_START)) {
            long reloadStart = tool.getPersistentData().getInt(RevolverItem.KEY_RELOAD_START);
            long now = level.getGameTime();
            reloading = now >= reloadStart && now - reloadStart < ((RevolverItem) gun.getItem()).getReloadTime();
        }
        int roundShots = tool.getPersistentData().getInt(RevolverItem.KEY_ROUND_SHOTS);
        fired = new boolean[slotCount];
        if (reloading) {
            for (int i = 0; i < slotCount; i++) {
                if (shots[i] >= 0) fired[i] = true;
            }
        } else if (roundShots > 0) {
            int idx = rawChamber;
            int scanned = 0;
            for (int i = 0; i < slotCount && scanned < roundShots; i++) {
                idx = (idx - 1 + slotCount) % slotCount;
                if (shots[idx] >= 0) {
                    fired[idx] = true;
                    scanned++;
                }
            }
        }

        currentChamber = rawChamber;
        if (currentChamber >= 0 && currentChamber < shots.length && shots[currentChamber] < 0) {
            currentChamber = findNextLoaded(rawChamber);
        }
    }

    private static int findNextLoaded(int startSlot) {
        if (shots.length == 0) return -1;
        for (int i = 1; i <= shots.length; i++) {
            int idx = (startSlot + i) % shots.length;
            if (shots[idx] >= 0) return idx;
        }
        return -1;
    }
}
