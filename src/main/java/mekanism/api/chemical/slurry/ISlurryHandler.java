package mekanism.api.chemical.slurry;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IMekanismChemicalHandler;
import mekanism.api.chemical.ISidedChemicalHandler;

public interface ISlurryHandler extends IChemicalHandler<Slurry, SlurryStack>, IEmptySlurryProvider, ComponentV3, AutoSyncedComponent {

    /**
     * A sided variant of {@link ISlurryHandler}
     */
    interface ISidedSlurryHandler extends ISidedChemicalHandler<Slurry, SlurryStack>, ISlurryHandler {
    }

    interface IMekanismSlurryHandler extends IMekanismChemicalHandler<Slurry, SlurryStack, ISlurryTank>, ISidedSlurryHandler {
    }
}