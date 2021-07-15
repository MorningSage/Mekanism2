package mekanism.common.network.packet.to_server;

import mekanism.common.Mekanism;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.network.packet.to_client.RespondServerString;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.thread.ThreadExecutor;

import java.util.UUID;

public class RequestServerString implements IMekanismPacket {
    private final UUID requesterUuid;

    public RequestServerString(UUID uuid) {
        Mekanism.logger.info("building request for String - SERVER/CLIENT");
        this.requesterUuid = uuid;
    }

    @Override
    public void handle(ThreadExecutor<?> executor, PlayerEntity playerEntity) {
        Mekanism.logger.info("handling request for String - SERVER");
        //Mekanism.packetHander.sendToClient((ServerPlayerEntity) playerEntity, new RespondServerString(Mekanism.ON_SERVER));
    }

    @Override
    public void encode(PacketByteBuf buffer) {
        Mekanism.logger.info("encoding request for String - CLIENT");
        buffer.writeUuid(requesterUuid);
    }

    public static RequestServerString decode(PacketByteBuf buffer) {
        Mekanism.logger.info("decoding request for String - SERVER");
        return new RequestServerString(buffer.readUuid());
    }
}
