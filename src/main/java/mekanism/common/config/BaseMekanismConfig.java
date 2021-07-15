package mekanism.common.config;

import mekanism.common.config.value.CachedPrimitiveValue;
import mekanism.common.config.value.CachedResolvableConfigValue;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMekanismConfig implements IMekanismConfig {
    private final List<CachedResolvableConfigValue<?, ?>> cachedConfigValues = new ArrayList<>();
    private final List<CachedPrimitiveValue<?>> cachedPrimitiveValues = new ArrayList<>();
    private final String modId;

    public BaseMekanismConfig(String modId) {
        this.modId = modId;
    }

    @Override
    public String getModId() {
        return this.modId;
    }

    @Override
    public void clearCache() {
        cachedConfigValues.forEach(CachedResolvableConfigValue::clearCache);
        cachedPrimitiveValues.forEach(CachedPrimitiveValue::clearCache);
    }

    @Override
    public <T, R> void addCachedValue(CachedResolvableConfigValue<T, R> configValue) {
        cachedConfigValues.add(configValue);
    }

    @Override
    public <T> void addCachedValue(CachedPrimitiveValue<T> configValue) {
        cachedPrimitiveValues.add(configValue);
    }
}
