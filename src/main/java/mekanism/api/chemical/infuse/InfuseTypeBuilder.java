package mekanism.api.chemical.infuse;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InfuseTypeBuilder extends ChemicalBuilder<InfuseType, InfuseTypeBuilder> {

    protected InfuseTypeBuilder(Identifier texture) {
        super(texture);
    }

    /**
     * Creates a builder for registering an {@link InfuseType}, using our default {@link InfuseType} texture.
     *
     * @return A builder for creating an {@link InfuseType}.
     */
    public static InfuseTypeBuilder builder() {
        return builder(new Identifier(MekanismAPI.MEKANISM_MODID, "infuse_type/base"));
    }

    /**
     * Creates a builder for registering an {@link InfuseType}, with a given texture.
     *
     * @param texture A {@link Identifier} representing the texture this {@link InfuseType} will use.
     *
     * @return A builder for creating an {@link InfuseType}.
     *
     * @apiNote The texture will be automatically stitched.
     * <br>
     * It is recommended to override {@link InfuseType#getColorRepresentation()} if this builder method is not used in combination with {@link #color(int)} due to the
     * texture not needing tinting.
     */
    public static InfuseTypeBuilder builder(Identifier texture) {
        return new InfuseTypeBuilder(Objects.requireNonNull(texture));
    }
}
