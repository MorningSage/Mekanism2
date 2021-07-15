package mekanism.api.chemical.gas;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IMekanismChemicalHandler;
import mekanism.api.chemical.ISidedChemicalHandler;

public interface IGasHandler extends IChemicalHandler<Gas, GasStack>, IEmptyGasProvider, ComponentV3, AutoSyncedComponent {

    /**
     * A sided variant of {@link IGasHandler}
     */
    interface ISidedGasHandler extends ISidedChemicalHandler<Gas, GasStack>, IGasHandler {
    }

    interface IMekanismGasHandler extends IMekanismChemicalHandler<Gas, GasStack, IGasTank>, ISidedGasHandler {
    }
}