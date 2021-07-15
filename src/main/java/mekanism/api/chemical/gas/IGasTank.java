package mekanism.api.chemical.gas;

import mekanism.api.NBTConstants;
import mekanism.api.chemical.IChemicalTank;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Convenience extension to make working with generics easier.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IGasTank extends IChemicalTank<Gas, GasStack>, IEmptyGasProvider {

    @Override
    default GasStack createStack(GasStack stored, long size) {
        return new GasStack(stored, size);
    }

    @Override
    default void deserializeNBT(NbtCompound nbt) {
        if (nbt.contains(NBTConstants.STORED, NBTConstants.TAG_TYPE_COMPOUND)) {
            setStackUnchecked(GasStack.readFromNBT(nbt.getCompound(NBTConstants.STORED)));
        }
    }
}
