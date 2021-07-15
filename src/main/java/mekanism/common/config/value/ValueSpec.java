package mekanism.common.config.value;

/*
 * This class is based on the MinecraftForge's implementation of
 * their Night-Config wrapper.  This class only is licensed under
 * the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation version 2.1 of the License.
 */

import mekanism.common.config.TomlConfig;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ValueSpec {
    private final String comment;
    private final String langKey;
    private final Range<?> range;
    private final boolean worldRestart;
    private final Class<?> clazz;
    private final Supplier<?> supplier;
    private final Predicate<Object> validator;
    private Object _default = null;

    public ValueSpec(Supplier<?> supplier, Predicate<Object> validator, TomlConfig.BuilderContext context) {
        Objects.requireNonNull(supplier, "Default supplier can not be null");
        Objects.requireNonNull(validator, "Validator can not be null");

        this.comment = context.hasComment() ? context.buildComment() : null;
        this.langKey = context.getTranslationKey();
        this.range = context.getRange();
        this.worldRestart = context.needsWorldRestart();
        this.clazz = context.getClazz();
        this.supplier = supplier;
        this.validator = validator;
    }

    public String getComment() { return comment; }
    public String getTranslationKey() { return langKey; }
    @SuppressWarnings("unchecked")
    public <V extends Comparable<? super V>> Range<V> getRange() { return (Range<V>)this.range; }
    public boolean needsWorldRestart() { return this.worldRestart; }
    public Class<?> getClazz(){ return this.clazz; }
    public boolean test(Object value) { return validator.test(value); }
    public Object correct(Object value) { return range == null ? getDefault() : range.correct(value, getDefault()); }

    public Object getDefault() {
        if (_default == null) _default = supplier.get();
        return _default;
    }
}
