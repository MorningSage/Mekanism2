package mekanism.common.lib.radiation.capability;

import mekanism.api.radiation.capability.IRadiationShielding;
import net.minecraft.nbt.NbtCompound;

public class DefaultRadiationShielding implements IRadiationShielding {
    @Override
    public double getRadiationShielding() {
        return 0;
    }

    @Override public void readFromNbt(NbtCompound tag) { }
    @Override public void writeToNbt(NbtCompound tag) { }
}

