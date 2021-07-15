package mekanism.api.chemical.pigment;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IMekanismChemicalHandler;
import mekanism.api.chemical.ISidedChemicalHandler;

public interface IPigmentHandler extends IChemicalHandler<Pigment, PigmentStack>, IEmptyPigmentProvider, ComponentV3, AutoSyncedComponent {

    /**
     * A sided variant of {@link IPigmentHandler}
     */
    interface ISidedPigmentHandler extends ISidedChemicalHandler<Pigment, PigmentStack>, IPigmentHandler {
    }

    interface IMekanismPigmentHandler extends IMekanismChemicalHandler<Pigment, PigmentStack, IPigmentTank>, ISidedPigmentHandler {
    }
}
