package com.ssakura49.sakuratinker_tools.network.s2c;

import com.ssakura49.sakura_library.client.SafeClientAccess;
import com.ssakura49.sakuratinker_tools.client.recoil.RecoilHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RecoilPacket {
    private final float recoil;

    public RecoilPacket(float recoilAmount) {
        this.recoil = recoilAmount;
    }

    public RecoilPacket(FriendlyByteBuf buf) {
        this.recoil = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(this.recoil);
    }

    public static RecoilPacket decode(FriendlyByteBuf buf) {
        return new RecoilPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getDirection().getReceptionSide().isClient()) {
                this.handleClient();
            }
        });
        context.get().setPacketHandled(true);
    }

    private void handleClient() {
        Player player = SafeClientAccess.getPlayer();
        if (player != null) {
//            RecoilHandler handler = RecoilHandler.getHandler(player);
//            if (System.currentTimeMillis() - handler.getLastRecoilTime() > 300L) {
//                handler.applyRecoil(player, this.recoil);
//            }
            RecoilHandler.getHandler(player).applyRecoil(player, this.recoil);
        }
    }
}
