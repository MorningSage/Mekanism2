package mekanism.common.capabilities.basic;

import mekanism.api.lasers.ILaserDissipation;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class DefaultLaserDissipation implements ILaserDissipation {

    @Override
    public double getDissipationPercent() {
        return 0;
    }

    @Override
    public double getRefractionPercent() {
        return 0;
    }

    @Override public void readFromNbt(@NotNull NbtCompound tag) { }
    @Override public void writeToNbt(@NotNull NbtCompound tag) { }
}