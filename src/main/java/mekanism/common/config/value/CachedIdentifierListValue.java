package mekanism.common.config.value;

import mekanism.common.config.IMekanismConfig;
import mekanism.common.config.TomlConfig;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CachedIdentifierListValue extends CachedResolvableConfigValue<List<Identifier>, List<? extends String>> {

    private static final Supplier<List<? extends String>> EMPTY = ArrayList::new;

    private CachedIdentifierListValue(IMekanismConfig config, ConfigValue<List<? extends String>> internal) {
        super(config, internal);
    }

    public static CachedIdentifierListValue define(IMekanismConfig config, TomlConfig.Builder builder, String path, Predicate<@NotNull Identifier> rlValidator) {
        return new CachedIdentifierListValue(config, builder.defineListAllowEmpty(Collections.singletonList(path), EMPTY, o -> {
            if (o instanceof String) {
                Identifier rl = Identifier.tryParse(((String) o).toLowerCase(Locale.ROOT));
                if (rl != null) {
                    return rlValidator.test(rl);
                }
            }
            return false;
        }));
    }

    @Override
    protected List<Identifier> resolve(List<? extends String> encoded) {
        //We ignore any strings that are invalid resource locations
        // validation should have happened before we got here, but in case something went wrong we don't want to crash and burn
        return encoded.stream().map(s -> Identifier.tryParse(s.toLowerCase(Locale.ROOT))).filter(Objects::nonNull).collect(Collectors.toCollection(() -> new ArrayList<>(encoded.size())));
    }

    @Override
    protected List<? extends String> encode(List<Identifier> values) {
        return values.stream().map(Identifier::toString).collect(Collectors.toCollection(() -> new ArrayList<>(values.size())));
    }
}