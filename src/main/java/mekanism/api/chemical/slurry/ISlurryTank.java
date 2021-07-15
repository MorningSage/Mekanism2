package mekanism.api.chemical.slurry;

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
public interface ISlurryTank extends IChemicalTank<Slurry, SlurryStack>, IEmptySlurryProvider {

    @Override
    default SlurryStack createStack(SlurryStack stored, long size) {
        return new SlurryStack(stored, size);
    }

    @Override
    default void deserializeNBT(NbtCompound nbt) {
        if (nbt.contains(NBTConstants.STORED, NBTConstants.TAG_TYPE_COMPOUND)) {
            setStackUnchecked(SlurryStack.readFromNBT(nbt.getCompound(NBTConstants.STORED)));
        }
    }
}
