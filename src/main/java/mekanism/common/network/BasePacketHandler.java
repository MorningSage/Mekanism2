package mekanism.common.network;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class BasePacketHandler {
    protected final Identifier identifier;
    protected final Short2ObjectArrayMap<Function<PacketByteBuf, ? extends IMekanismPacket>> indicies;
    protected final Object2ObjectArrayMap<Class<?>, Pair<Short, BiConsumer<IMekanismPacket, PacketByteBuf>>> types;

    protected int index = 0;

    public BasePacketHandler(Identifier identifier) {
        this.identifier = identifier;
        this.indicies = new Short2ObjectArrayMap<>();
        this.types = new Object2ObjectArrayMap<>();
    }

    public static void log(String logFormat, Object... params) {
        //TODO: Add more logging for packets using this
        //if (MekanismConfig.general.logPackets.get()) {
        //    Mekanism.logger.info(logFormat, params);
        //}
    }

    public void initialize() {
        registerPackets((type, decoder) -> {
            short i = (short)(index++ & 0xff);
            indicies.put(i, decoder);
            types.put(type, new Pair<>(i, IMekanismPacket::encode));
        });

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            registerClientCallback();
        } else {
            registerServerCallback();
        }
    }
    protected abstract void registerPackets(PacketRegister registry);

    @Environment(EnvType.CLIENT)
    void registerClientCallback() {
        ClientPlayNetworking.registerGlobalReceiver(identifier, this::receive);
    }

    @Environment(EnvType.SERVER)
    void registerServerCallback() {
        ServerPlayNetworking.registerGlobalReceiver(identifier, this::receive);
    }

    @Environment(EnvType.CLIENT)
    public <MSG extends IMekanismPacket> void sendToServer(MSG packet) {
        ClientPlayNetworking.send(this.identifier, encodePacket(packet));
    }

    @Environment(EnvType.SERVER)
    public <MSG extends IMekanismPacket> void sendToClient(ServerPlayerEntity player, MSG packet) {
        ServerPlayNetworking.send(player, this.identifier, encodePacket(packet));
    }

    @Environment(EnvType.CLIENT)
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IMekanismPacket.handle(indicies.get(buf.readByte()).apply(buf), client, client.player);
    }

    @Environment(EnvType.SERVER)
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IMekanismPacket.handle(indicies.get(buf.readByte()).apply(buf), server, player);
    }

    protected <MSG extends IMekanismPacket> PacketByteBuf encodePacket(MSG packet) {
        PacketByteBuf buffer = PacketByteBufs.create();
        Pair<Short, BiConsumer<IMekanismPacket, PacketByteBuf>> packetInfo = types.get(packet.getClass());
        buffer.writeByte(packetInfo.getLeft());
        packetInfo.getRight().accept(packet, buffer);
        return buffer;
    }

    protected interface PacketRegister {
        void register(Class<? extends IMekanismPacket> type, Function<PacketByteBuf, ? extends IMekanismPacket> decoder);
    }

}
