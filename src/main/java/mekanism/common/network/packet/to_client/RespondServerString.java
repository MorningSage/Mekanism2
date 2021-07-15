package mekanism.common.network.packet.to_client;

import mekanism.common.Mekanism;
import mekanism.common.network.IMekanismPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.thread.ThreadExecutor;

public class RespondServerString implements IMekanismPacket {
    private final String serverString;

    public RespondServerString(String serverString) {
        Mekanism.logger.info("creating response for String - SERVER/CLIENT");
        this.serverString = serverString;
    }

    @Override
    public void handle(ThreadExecutor<?> executor, PlayerEntity playerEntity) {
        Mekanism.logger.info("handing response for String - CLIENT");
        System.out.println(serverString);
    }

    @Override
    public void encode(PacketByteBuf buffer) {
        Mekanism.logger.info("encoding response for String - SERVER");
        buffer.writeString(serverString);
    }

    public static RespondServerString decode(PacketByteBuf buffer) {
        Mekanism.logger.info("decoding response for String - CLIENT");
        return new RespondServerString(buffer.readString());
    }
}
