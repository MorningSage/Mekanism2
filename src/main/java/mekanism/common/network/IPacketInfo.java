package mekanism.common.network;

import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface IPacketInfo {
    Class<? extends IMekanismPacket> getType();
    Function<PacketByteBuf, ? extends IMekanismPacket> getDecoder();
    BiConsumer<IMekanismPacket, PacketByteBuf> getEncoder();
}
