package mekanism.common.capabilities.basic;

import mekanism.api.IEvaporationSolar;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class DefaultEvaporationSolar implements IEvaporationSolar {

    @Override
    public boolean canSeeSun() {
        return false;
    }

    @Override public void readFromNbt(@NotNull NbtCompound tag) { }
    @Override public void writeToNbt(@NotNull NbtCompound tag) { }

}