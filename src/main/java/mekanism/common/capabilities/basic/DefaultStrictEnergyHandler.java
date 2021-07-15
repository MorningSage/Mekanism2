package mekanism.common.capabilities.basic;

import mekanism.api.Action;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.math.FloatingLong;
import mekanism.api.toport.INBTSerializable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DefaultStrictEnergyHandler implements IStrictEnergyHandler {

    @Override
    public int getEnergyContainerCount() {
        return 1;
    }

    @Override
    public FloatingLong getEnergy(int container) {
        return FloatingLong.ZERO;
    }

    @Override
    public void setEnergy(int container, FloatingLong energy) {
    }

    @Override
    public FloatingLong getMaxEnergy(int container) {
        return FloatingLong.ZERO;
    }

    @Override
    public FloatingLong getNeededEnergy(int container) {
        return FloatingLong.ZERO;
    }

    @Override
    public FloatingLong insertEnergy(int container, FloatingLong amount, Action action) {
        return amount;
    }

    @Override
    public FloatingLong extractEnergy(int container, FloatingLong amount, Action action) {
        return FloatingLong.ZERO;
    }

    @Override public void readFromNbt(@NotNull NbtCompound tag) {
        if (this instanceof INBTSerializable) {
            Class<? extends NbtElement> nbtClass = ((INBTSerializable<? extends NbtElement>) this).serializeNBT().getClass();
            if (nbtClass.isInstance(tag)) {
                ((INBTSerializable) this).deserializeNBT(nbtClass.cast(tag));
            }
        }
    }
    @Override public void writeToNbt(@NotNull NbtCompound tag) {
        if (this instanceof INBTSerializable) {
            tag.put("defaultEnergy", ((INBTSerializable<?>) this).serializeNBT());
        }
    }
}