package com.ssakura49.sakuratinker_tools.network;

import com.ssakura49.sakuratinker_tools.network.s2c.CollectedDropsSync;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
   private static final String PROTOCOL_VERSION = "1";
   public static final SimpleChannel INSTANCE = ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath("sakuratinker_tools", "main")).networkProtocolVersion(() -> "1").clientAcceptedVersions("1"::equals).serverAcceptedVersions("1"::equals).simpleChannel();
   private static int id = 0;

   public static void init() {
      INSTANCE.messageBuilder(CollectedDropsSync.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CollectedDropsSync::new).encoder(CollectedDropsSync::encode).consumerNetworkThread(CollectedDropsSync::handle).add();
   }

   public static <MSG> void sendToServer(MSG msg) {
      INSTANCE.sendToServer(msg);
   }

   public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
   }

   public static <MSG> void sendToClient(MSG msg) {
      INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
   }

   public static <MSG> void sendToAll(MSG msg) {
      INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
   }

   public static <MSG> void sentToTrackingChunk(LevelChunk chunk, MSG msg) {
      INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
   }

   public static <MSG> void sentToTrackingEntity(Entity entity, MSG msg) {
      INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
   }

   public static <MSG> void sentToTrackingEntityAndPlayer(Entity entity, MSG msg) {
      INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
   }

   public static <MSG> void sendToClient(ServerPlayer player, MSG msg) {
      INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
   }
}
