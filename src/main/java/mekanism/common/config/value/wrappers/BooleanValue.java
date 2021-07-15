package mekanism.common.config.value.wrappers;

/*
 * This class is based on the MinecraftForge's implementation of
 * their Night-Config wrapper.  This class only is licensed under
 * the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation version 2.1 of the License.
 */

import mekanism.common.config.TomlConfig;
import mekanism.common.config.value.ConfigValue;

import java.util.List;
import java.util.function.Supplier;

public class BooleanValue extends ConfigValue<Boolean> {
    public BooleanValue(TomlConfig.Builder parent, List<String> path, Supplier<Boolean> defaultSupplier) {
        super(parent, path, defaultSupplier);
    }
}
