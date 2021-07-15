package mekanism.api.chemical;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.chemical.attribute.ChemicalAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Map;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChemicalBuilder<CHEMICAL extends Chemical<CHEMICAL>, BUILDER extends ChemicalBuilder<CHEMICAL, BUILDER>> {

    private final Map<Class<? extends ChemicalAttribute>, ChemicalAttribute> attributeMap = new Object2ObjectOpenHashMap<>();
    private final Identifier texture;
    private int color = 0xFFFFFF;
    private boolean hidden;

    protected ChemicalBuilder(Identifier texture) {
        this.texture = texture;
    }

    /**
     * Adds a {@link ChemicalAttribute} to the set of attributes this chemical has.
     *
     * @param attribute Attribute to add.
     */
    public BUILDER with(ChemicalAttribute attribute) {
        attributeMap.put(attribute.getClass(), attribute);
        return getThis();
    }

    /**
     * Gets the attributes this chemical will have.
     */
    public Map<Class<? extends ChemicalAttribute>, ChemicalAttribute> getAttributeMap() {
        return Collections.unmodifiableMap(attributeMap);
    }

    /**
     * Gets the {@link Identifier} representing the texture this chemical will use.
     */
    public Identifier getTexture() {
        return texture;
    }

    /**
     * Sets the tint to apply to this chemical when rendering.
     *
     * @param color Color in RRGGBB format
     */
    public BUILDER color(int color) {
        this.color = color;
        return getThis();
    }

    /**
     * Marks that this chemical will be hidden in JEI, and not included in the preset of filled chemical tanks.
     */
    public BUILDER hidden() {
        this.hidden = true;
        return getThis();
    }

    /**
     * Gets the tint to apply to this chemical when rendering.
     *
     * @return Tint in RRGGBB format.
     */
    public int getColor() {
        return color;
    }

    /**
     * Checks if this chemical should be hidden from JEI and not included in the preset of filled chemical tanks.
     *
     * @return {@code true} if it should be hidden.
     */
    public boolean isHidden() {
        return hidden;
    }

    @SuppressWarnings("unchecked")
    protected BUILDER getThis() {
        return (BUILDER) this;
    }
}
