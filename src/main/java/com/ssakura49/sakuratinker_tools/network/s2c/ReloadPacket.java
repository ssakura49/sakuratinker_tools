package com.ssakura49.sakuratinker_tools.network.s2c;

import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableGunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadPacket {
    public ReloadPacket() {
    }

    public ReloadPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static ReloadPacket decode(FriendlyByteBuf buf) {
        return new ReloadPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof ModifiableGunItem item) {
                    item.tryReload(stack, player.level(), player);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
