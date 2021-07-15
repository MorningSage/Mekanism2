package mekanism.common.config.value;

/*
 * This class is based on the MinecraftForge's implementation of
 * their Night-Config wrapper.  This class only is licensed under
 * the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation version 2.1 of the License.
 */


import com.electronwill.nightconfig.core.Config;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import mekanism.common.config.TomlConfig;
import mekanism.common.config.TomlConfig.Builder;

import java.util.List;
import java.util.function.Supplier;

public class ConfigValue<T> {
    @VisibleForTesting
    static boolean USE_CACHES = true;

    private final Builder parent;
    private final List<String> path;
    private final Supplier<T> defaultSupplier;

    private T cachedValue = null;

    public TomlConfig spec;

    public ConfigValue(Builder parent, List<String> path, Supplier<T> defaultSupplier) {
        this.parent = parent;
        this.path = path;
        this.defaultSupplier = defaultSupplier;
        this.parent.values.add(this);
    }

    public List<String> getPath() {
        return Lists.newArrayList(path);
    }

    public T get() {
        Preconditions.checkNotNull(spec, "Cannot get config value before spec is built");
        if (spec.childConfig == null) return defaultSupplier.get();

        if (USE_CACHES && cachedValue == null) {
            cachedValue = getRaw(spec.childConfig, path, defaultSupplier);
        } else if (!USE_CACHES) {
            return getRaw(spec.childConfig, path, defaultSupplier);
        }

        return cachedValue;
    }

    protected T getRaw(Config config, List<String> path, Supplier<T> defaultSupplier) {
        return config.getOrElse(path, defaultSupplier);
    }

    public Builder next() {
        return parent;
    }

    public void save() {
        Preconditions.checkNotNull(spec, "Cannot save config value before spec is built");
        Preconditions.checkNotNull(spec.childConfig, "Cannot save config value without assigned Config object present");
        spec.save();
    }

    public void set(T value) {
        Preconditions.checkNotNull(spec, "Cannot set config value before spec is built");
        Preconditions.checkNotNull(spec.childConfig, "Cannot set config value without assigned Config object present");
        spec.childConfig.set(path, value);
        this.cachedValue = value;
    }

    public void clearCache() {
        this.cachedValue = null;
    }
}
