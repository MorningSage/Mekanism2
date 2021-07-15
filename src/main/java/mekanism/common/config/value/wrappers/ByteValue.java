package mekanism.common.config.value.wrappers;

import com.electronwill.nightconfig.core.Config;
import mekanism.common.config.TomlConfig;
import mekanism.common.config.value.ConfigValue;

import java.util.List;
import java.util.function.Supplier;

public class ByteValue extends ConfigValue<Byte> {
    public ByteValue(TomlConfig.Builder parent, List<String> path, Supplier<Byte> defaultSupplier) {
        super(parent, path, defaultSupplier);
    }

    @Override
    protected Byte getRaw(Config config, List<String> path, Supplier<Byte> defaultSupplier) {
        return config.getByteOrElse(path, defaultSupplier.get());
    }
}

