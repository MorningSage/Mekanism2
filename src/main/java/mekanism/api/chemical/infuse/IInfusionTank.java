package mekanism.api.chemical.infuse;

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
public interface IInfusionTank extends IChemicalTank<InfuseType, InfusionStack>, IEmptyInfusionProvider {

    @Override
    default InfusionStack createStack(InfusionStack stored, long size) {
        return new InfusionStack(stored, size);
    }

    @Override
    default void deserializeNBT(NbtCompound nbt) {
        if (nbt.contains(NBTConstants.STORED, NBTConstants.TAG_TYPE_COMPOUND)) {
            setStackUnchecked(InfusionStack.readFromNBT(nbt.getCompound(NBTConstants.STORED)));
        }
    }
}
