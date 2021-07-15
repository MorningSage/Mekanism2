package mekanism.common.config.value.wrappers;

/*
 * This class is based on the MinecraftForge's implementation of
 * their Night-Config wrapper.  This class only is licensed under
 * the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation version 2.1 of the License.
 */

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.EnumGetMethod;
import mekanism.common.config.TomlConfig;
import mekanism.common.config.value.ConfigValue;

import java.util.List;
import java.util.function.Supplier;

public class EnumValue<T extends Enum<T>> extends ConfigValue<T> {
    private final EnumGetMethod converter;
    private final Class<T> clazz;

    public EnumValue(TomlConfig.Builder parent, List<String> path, Supplier<T> defaultSupplier, EnumGetMethod converter, Class<T> clazz) {
        super(parent, path, defaultSupplier);
        this.converter = converter;
        this.clazz = clazz;
    }

    @Override
    protected T getRaw(Config config, List<String> path, Supplier<T> defaultSupplier) {
        return config.getEnumOrElse(path, clazz, converter, defaultSupplier);
    }
}
