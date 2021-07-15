package mekanism.client;

import mekanism.common.registration.impl.FluidRegistryObject;
import mekanism.common.registries.MekanismFluids;
import net.fabricmc.api.ClientModInitializer;

public class MekanismClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (FluidRegistryObject<?, ?, ?, ?> fluid : MekanismFluids.FLUIDS.getAllFluids()) {
            fluid.setupFluidRendering();
        }
    }

}
