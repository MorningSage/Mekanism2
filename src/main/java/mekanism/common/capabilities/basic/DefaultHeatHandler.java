package mekanism.common.capabilities.basic;

import mekanism.api.heat.HeatAPI;
import mekanism.api.heat.IHeatHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DefaultHeatHandler implements IHeatHandler {

    @Override
    public int getHeatCapacitorCount() {
        return 0;
    }

    @Override
    public double getTemperature(int capacitor) {
        return HeatAPI.AMBIENT_TEMP;
    }

    @Override
    public double getInverseConduction(int capacitor) {
        return HeatAPI.DEFAULT_INVERSE_CONDUCTION;
    }

    @Override
    public double getHeatCapacity(int capacitor) {
        return HeatAPI.DEFAULT_HEAT_CAPACITY;
    }

    @Override
    public void handleHeat(int capacitor, double transfer) {
    }

    @Override public void readFromNbt(NbtCompound tag) { }
    @Override public void writeToNbt(NbtCompound tag) { }
}