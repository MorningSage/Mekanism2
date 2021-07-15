package mekanism.common.network;

import mekanism.common.Mekanism;
import mekanism.common.network.packet.to_client.*;
import mekanism.common.network.packet.to_server.*;

public class PacketHandler extends BasePacketHandler {

    public PacketHandler() { super(Mekanism.id(Mekanism.MODID)); }

    @Override
    public void registerPackets(PacketRegister registry) {
        registry.register(RespondServerString.class, RespondServerString::decode);
        registry.register(Test2S2CPacket.class, Test2S2CPacket::decode);

        registry.register(RequestServerString.class, RequestServerString::decode);
        registry.register(Test2C2SPacket.class, Test2C2SPacket::decode);
    }
}
