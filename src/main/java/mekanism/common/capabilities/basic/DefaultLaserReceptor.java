package mekanism.common.capabilities.basic;

import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.FloatingLong;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class DefaultLaserReceptor implements ILaserReceptor {

    @Override
    public void receiveLaserEnergy(@Nonnull FloatingLong energy, Direction side) {

    }

    @Override
    public boolean canLasersDig() {
        return false;
    }

    @Override public void readFromNbt(@NotNull NbtCompound tag) { }
    @Override public void writeToNbt(@NotNull NbtCompound tag) { }
}