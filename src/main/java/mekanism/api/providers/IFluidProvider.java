package mekanism.api.providers;

import net.minecraft.fluid.Fluid;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface IFluidProvider extends IBaseProvider {

    /**
     * Gets the fluid this this provider represents.
     */
    @NotNull
    Fluid getFluid();

    @Override
    default Identifier getRegistryName() {
        return Registry.FLUID.getId(getFluid());
    }

    @Override
    default Text getTextComponent() {
        //return getFluid().getAttributes().getDisplayName(getFluidStack(1));
        return Text.of("getTextComponent");
    }

    @Override
    default String getTranslationKey() {
        //return getFluid().getAttributes().getTranslationKey();
        return "getTranslationKey";
    }
}
