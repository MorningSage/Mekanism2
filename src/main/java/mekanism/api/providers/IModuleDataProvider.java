package mekanism.api.providers;

import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.ModuleData;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;

public interface IModuleDataProvider<MODULE extends ICustomModule<MODULE>> extends IBaseProvider {

    /**
     * Gets the module data this provider represents.
     */
    @Nonnull
    ModuleData<MODULE> getModuleData();

    @Override
    default Identifier getRegistryName() {
        return getModuleData().getRegistryName();
    }

    @Override
    default String getTranslationKey() {
        return getModuleData().getTranslationKey();
    }
}