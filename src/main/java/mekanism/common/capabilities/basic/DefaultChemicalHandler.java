package mekanism.common.capabilities.basic;

import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class DefaultChemicalHandler<CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>> implements IChemicalHandler<CHEMICAL, STACK> {

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public STACK getChemicalInTank(int tank) {
        return getEmptyStack();
    }

    @Override
    public void setChemicalInTank(int tank, STACK stack) {
    }

    @Override
    public long getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isValid(int tank, STACK stack) {
        return true;
    }

    @Override
    public STACK insertChemical(int tank, STACK stack, Action action) {
        return stack;
    }

    @Override
    public STACK extractChemical(int tank, long amount, Action action) {
        return getEmptyStack();
    }

    public static class DefaultGasHandler extends DefaultChemicalHandler<Gas, GasStack> implements IGasHandler {
        @Override public void readFromNbt(NbtCompound tag) { }
        @Override public void writeToNbt(NbtCompound tag) { }
    }

    public static class DefaultInfusionHandler extends DefaultChemicalHandler<InfuseType, InfusionStack> implements IInfusionHandler {
        @Override public void readFromNbt(NbtCompound tag) { }
        @Override public void writeToNbt(NbtCompound tag) { }
    }

    public static class DefaultPigmentHandler extends DefaultChemicalHandler<Pigment, PigmentStack> implements IPigmentHandler {
        @Override public void readFromNbt(NbtCompound tag) { }
        @Override public void writeToNbt(NbtCompound tag) { }
    }

    public static class DefaultSlurryHandler extends DefaultChemicalHandler<Slurry, SlurryStack> implements ISlurryHandler {
        @Override public void readFromNbt(NbtCompound tag) { }
        @Override public void writeToNbt(NbtCompound tag) { }
    }
}