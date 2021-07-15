package mekanism.common.registries;

import mekanism.common.ChemicalConstants;
import mekanism.common.Mekanism;
import mekanism.common.fluid.MekanismFluid.Flowing;
import mekanism.common.fluid.MekanismFluid.Properties;
import mekanism.common.fluid.MekanismFluid.Still;
import mekanism.common.registration.impl.FluidDeferredRegister;
import mekanism.common.registration.impl.FluidRegistryObject;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.BucketItem;
import net.minecraft.util.Identifier;

public class MekanismFluids {

    private MekanismFluids() {
    }

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(Mekanism.MODID);

    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> HYDROGEN = FLUIDS.registerLiquidChemical(ChemicalConstants.HYDROGEN);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> OXYGEN = FLUIDS.registerLiquidChemical(ChemicalConstants.OXYGEN);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> CHLORINE = FLUIDS.registerLiquidChemical(ChemicalConstants.CHLORINE);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> SULFUR_DIOXIDE = FLUIDS.registerLiquidChemical(ChemicalConstants.SULFUR_DIOXIDE);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> SULFUR_TRIOXIDE = FLUIDS.registerLiquidChemical(ChemicalConstants.SULFUR_TRIOXIDE);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> SULFURIC_ACID = FLUIDS.registerLiquidChemical(ChemicalConstants.SULFURIC_ACID);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> HYDROGEN_CHLORIDE = FLUIDS.registerLiquidChemical(ChemicalConstants.HYDROGEN_CHLORIDE);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> HYDROFLUORIC_ACID = FLUIDS.registerLiquidChemical(ChemicalConstants.HYDROFLUORIC_ACID);
    //Internal gases
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> ETHENE = FLUIDS.registerLiquidChemical(ChemicalConstants.ETHENE);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> SODIUM = FLUIDS.registerLiquidChemical(ChemicalConstants.SODIUM);
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> BRINE = FLUIDS.register("brine", fluidAttributes -> fluidAttributes.color(0xFFFEEF9C));
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> LITHIUM = FLUIDS.registerLiquidChemical(ChemicalConstants.LITHIUM);

    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> STEAM = FLUIDS.register("steam",
        Properties.builder(Mekanism.id("liquid/steam"), Mekanism.id("liquid/steam_flow")).gaseous().temperature(373));
    public static final FluidRegistryObject<Still, Flowing, FluidBlock, BucketItem> HEAVY_WATER = FLUIDS.register("heavy_water",
        Properties.builder(new Identifier("block/water_still"), new Identifier("block/water_flow")).color(0xFF0D1455));
}
