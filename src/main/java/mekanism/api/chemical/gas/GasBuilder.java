package mekanism.api.chemical.gas;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GasBuilder extends ChemicalBuilder<Gas, GasBuilder> {

    protected GasBuilder(Identifier texture) {
        super(texture);
    }

    /**
     * Creates a builder for registering a {@link Gas}, using our default {@link Gas} texture.
     *
     * @return A builder for creating a {@link Gas}.
     */
    public static GasBuilder builder() {
        return builder(new Identifier(MekanismAPI.MEKANISM_MODID, "liquid/liquid"));
    }

    /**
     * Creates a builder for registering a {@link Gas}, with a given texture.
     *
     * @param texture A {@link Identifier} representing the texture this {@link Gas} will use.
     *
     * @return A builder for creating a {@link Gas}.
     *
     * @apiNote The texture will be automatically stitched.
     * <br>
     * It is recommended to override {@link Gas#getColorRepresentation()} if this builder method is not used in combination with {@link #color(int)} due to the texture
     * not needing tinting.
     */
    public static GasBuilder builder(Identifier texture) {
        return new GasBuilder(Objects.requireNonNull(texture));
    }
}
