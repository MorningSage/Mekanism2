package mekanism.common.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.thread.ThreadExecutor;

public interface IMekanismPacket {

    void handle(ThreadExecutor<?> executor, PlayerEntity playerEntity);

    void encode(PacketByteBuf buffer);

    static <PACKET extends IMekanismPacket> void handle(PACKET message, ThreadExecutor<?> executor, PlayerEntity playerEntity) {
        //Message should never be null unless something went horribly wrong decoding.
        // In which case we don't want to try enqueuing handling it, or set the packet as handled
        if (message != null) {
            // Must check ourselves as Minecraft will sometimes delay tasks even when they are received on the client thread
            // Same logic as ThreadTaskExecutor#runImmediately without the join
            if (!executor.isOnThread()) {
                //((ThreadExecutorAccessor) executor).submitAsync(() -> message.handle(executor, playerEntity)); // Use the internal method so thread check isn't done twice
            } else {
                message.handle(executor, playerEntity);
            }
        }
    }
}
