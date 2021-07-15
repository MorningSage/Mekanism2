package mekanism.api.chemical.infuse;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IMekanismChemicalHandler;
import mekanism.api.chemical.ISidedChemicalHandler;

public interface IInfusionHandler extends IChemicalHandler<InfuseType, InfusionStack>, IEmptyInfusionProvider, ComponentV3, AutoSyncedComponent {

    /**
     * A sided variant of {@link IInfusionHandler}
     */
    interface ISidedInfusionHandler extends ISidedChemicalHandler<InfuseType, InfusionStack>, IInfusionHandler {
    }

    interface IMekanismInfusionHandler extends IMekanismChemicalHandler<InfuseType, InfusionStack, IInfusionTank>, ISidedInfusionHandler {
    }
}
