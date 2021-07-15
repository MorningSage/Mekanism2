package mekanism.api.chemical.slurry;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalBuilder;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SlurryBuilder extends ChemicalBuilder<Slurry, SlurryBuilder> {

    @Nullable
    private Tag<Item> oreTag;

    protected SlurryBuilder(Identifier texture) {
        super(texture);
    }

    /**
     * Creates a builder for registering a {@link Slurry}, using our default clean {@link Slurry} texture.
     *
     * @return A builder for creating a {@link Slurry}.
     */
    public static SlurryBuilder clean() {
        return builder(new Identifier(MekanismAPI.MEKANISM_MODID, "slurry/clean"));
    }

    /**
     * Creates a builder for registering a {@link Slurry}, using our default dirty {@link Slurry} texture.
     *
     * @return A builder for creating a {@link Slurry}.
     */
    public static SlurryBuilder dirty() {
        return builder(new Identifier(MekanismAPI.MEKANISM_MODID, "slurry/dirty"));
    }

    /**
     * Creates a builder for registering a {@link Slurry}, with a given texture.
     *
     * @param texture A {@link Identifier} representing the texture this {@link Slurry} will use.
     *
     * @return A builder for creating a {@link Slurry}.
     *
     * @apiNote The texture will be automatically stitched.
     * <br>
     * It is recommended to override {@link Slurry#getColorRepresentation()} if this builder method is not used in combination with {@link #color(int)} due to the texture
     * not needing tinting.
     */
    public static SlurryBuilder builder(Identifier texture) {
        return new SlurryBuilder(Objects.requireNonNull(texture));
    }

    /**
     * Sets the tag that represents the ore that goes with this {@link Slurry}.
     *
     * @param oreTag Tag representing the ore.
     */
    public SlurryBuilder ore(Tag<Item> oreTag) {
        this.oreTag = Objects.requireNonNull(oreTag);
        return this;
    }

    /**
     * Gets the item tag that represents the ore that goes with this {@link Slurry}.
     */
    @Nullable
    public Tag<Item> getOreTag() {
        return oreTag;
    }
}
