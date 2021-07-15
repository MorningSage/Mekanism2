package mekanism.common.network.packet.to_client;

import mekanism.common.network.IMekanismPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.thread.ThreadExecutor;

public class Test2S2CPacket implements IMekanismPacket {

    @Override
    public void handle(ThreadExecutor<?> executor, PlayerEntity playerEntity) {

    }

    @Override
    public void encode(PacketByteBuf buffer) {

    }

    public static Test2S2CPacket decode(PacketByteBuf buffer) {
        return new Test2S2CPacket();
    }
}
