package mekanism.api.gear.config;

import net.minecraft.nbt.NbtCompound;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Boolean implementation of {@link ModuleConfigData}.
 */
public final class ModuleBooleanData implements ModuleConfigData<Boolean> {

    private boolean value;

    /**
     * Creates a new {@link ModuleBooleanData} with a default value of {@code true}.
     */
    public ModuleBooleanData() {
        this(true);
    }

    /**
     * Creates a new {@link ModuleBooleanData} with the given default value.
     *
     * @param def Default value.
     */
    public ModuleBooleanData(boolean def) {
        value = def;
    }

    @Nonnull
    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void set(@Nonnull Boolean val) {
        value = Objects.requireNonNull(val, "Value cannot be null.");
    }

    @Override
    public void read(@Nonnull String name, @Nonnull NbtCompound tag) {
        Objects.requireNonNull(tag, "Tag cannot be null.");
        Objects.requireNonNull(name, "Name cannot be null.");
        value = tag.getBoolean(name);
    }

    @Override
    public void write(@Nonnull String name, @Nonnull NbtCompound tag) {
        Objects.requireNonNull(tag, "Tag cannot be null.");
        Objects.requireNonNull(name, "Name cannot be null.");
        tag.putBoolean(name, value);
    }
}
