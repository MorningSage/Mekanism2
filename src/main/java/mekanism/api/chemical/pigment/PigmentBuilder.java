package mekanism.api.chemical.pigment;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PigmentBuilder extends ChemicalBuilder<Pigment, PigmentBuilder> {

    protected PigmentBuilder(Identifier texture) {
        super(texture);
    }

    /**
     * Creates a builder for registering a {@link Pigment}, using our default {@link Pigment} texture.
     *
     * @return A builder for creating a {@link Pigment}.
     */
    public static PigmentBuilder builder() {
        return builder(new Identifier(MekanismAPI.MEKANISM_MODID, "pigment/base"));
    }

    /**
     * Creates a builder for registering a {@link Pigment}, with a given texture.
     *
     * @param texture A {@link Identifier} representing the texture this {@link Pigment} will use.
     *
     * @return A builder for creating a {@link Pigment}.
     *
     * @apiNote The texture will be automatically stitched.
     * <br>
     * It is recommended to override {@link Pigment#getColorRepresentation()} if this builder method is not used in combination with {@link #color(int)} due to the
     * texture not needing tinting.
     */
    public static PigmentBuilder builder(Identifier texture) {
        return new PigmentBuilder(Objects.requireNonNull(texture));
    }
}
