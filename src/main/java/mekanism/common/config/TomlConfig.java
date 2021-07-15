package mekanism.common.config;

/*
 * This class is based on the MinecraftForge's implementation of
 * their Night-Config wrapper.  This class only is licensed under
 * the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation version 2.1 of the License.
 */

import com.electronwill.nightconfig.core.*;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.utils.UnmodifiableConfigWrapper;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import mekanism.common.config.value.ConfigValue;
import mekanism.common.config.value.Range;
import mekanism.common.config.value.ValueSpec;
import mekanism.common.config.value.wrappers.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.electronwill.nightconfig.core.ConfigSpec.CorrectionAction.*;

@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public class TomlConfig extends UnmodifiableConfigWrapper<UnmodifiableConfig> {
    public static final Marker CORE = MarkerManager.getMarker("CORE");
    private final Map<List<String>, String> levelComments;
    private final UnmodifiableConfig values;
    public Config childConfig;

    private boolean isCorrecting = false;

    private TomlConfig(UnmodifiableConfig storage, UnmodifiableConfig values, Map<List<String>, String> levelComments) {
        super(storage);
        this.values = values;
        this.levelComments = levelComments;
    }

    public void setConfig(CommentedConfig config) {
        this.childConfig = config;
        if (config != null && !isCorrect(config)) {
            String configName = config instanceof FileConfig ? ((FileConfig) config).getNioPath().toString() : config.toString();
            LogManager.getLogger().warn(CORE, "Configuration file {} is not correct. Correcting", configName);
            correct(config,
                (action, path, incorrectValue, correctedValue) ->
                    LogManager.getLogger().warn(CORE, "Incorrect key {} was corrected from {} to its default, {}. {}", DOT_JOINER.join( path ), incorrectValue, correctedValue, incorrectValue == correctedValue ? "This seems to be an error." : ""),
                (action, path, incorrectValue, correctedValue) ->
                    LogManager.getLogger().debug(CORE, "The comment on key {} does not match the spec. This may create a backup.", DOT_JOINER.join( path )));

            if (config instanceof FileConfig) {
                ((FileConfig) config).save();
            }
        }
        this.afterReload();
    }

    public boolean isCorrecting() {
        return isCorrecting;
    }

    public boolean isLoaded() {
        return childConfig != null;
    }

    public UnmodifiableConfig getSpec() {
        return this.config;
    }

    public UnmodifiableConfig getValues() {
        return this.values;
    }

    public void afterReload() {
        this.resetCaches(getValues().valueMap().values());
    }

    private void resetCaches(final Iterable<Object> configValues) {
        configValues.forEach(value -> {
            if (value instanceof ConfigValue) {
                final ConfigValue<?> configValue = (ConfigValue<?>) value;
                configValue.clearCache();
            } else if (value instanceof Config) {
                final Config innerConfig = (Config) value;
                this.resetCaches(innerConfig.valueMap().values());
            }
        });
    }

    public void save()
    {
        Preconditions.checkNotNull(childConfig, "Cannot save config value without assigned Config object present");
        if (childConfig instanceof FileConfig) {
            ((FileConfig)childConfig).save();
        }
    }

    public synchronized boolean isCorrect(CommentedConfig config) {
        LinkedList<String> parentPath = new LinkedList<>();
        return correct(this.config, config, parentPath, Collections.unmodifiableList( parentPath ), (a, b, c, d) -> {}, null, true) == 0;
    }

    public int correct(CommentedConfig config) {
        return correct(config, (action, path, incorrectValue, correctedValue) -> {}, null);
    }

    public synchronized int correct(CommentedConfig config, ConfigSpec.CorrectionListener listener) {
        return correct(config, listener, null);
    }

    public synchronized int correct(CommentedConfig config, ConfigSpec.CorrectionListener listener, ConfigSpec.CorrectionListener commentListener) {
        LinkedList<String> parentPath = new LinkedList<>(); //Linked list for fast add/removes
        int ret;
        try {
            isCorrecting = true;
            ret = correct(this.config, config, parentPath, Collections.unmodifiableList(parentPath), listener, commentListener, false);
        } finally {
            isCorrecting = false;
        }
        return ret;
    }

    private int correct(UnmodifiableConfig spec, CommentedConfig config, LinkedList<String> parentPath, List<String> parentPathUnmodifiable, ConfigSpec.CorrectionListener listener, ConfigSpec.CorrectionListener commentListener, boolean dryRun)
    {
        int count = 0;

        Map<String, Object> specMap = spec.valueMap();
        Map<String, Object> configMap = config.valueMap();

        for (Map.Entry<String, Object> specEntry : specMap.entrySet())
        {
            final String key = specEntry.getKey();
            final Object specValue = specEntry.getValue();
            final Object configValue = configMap.get(key);
            final ConfigSpec.CorrectionAction action = configValue == null ? ADD : REPLACE;

            parentPath.addLast(key);

            if (specValue instanceof Config)
            {
                if (configValue instanceof CommentedConfig)
                {
                    count += correct((Config)specValue, (CommentedConfig)configValue, parentPath, parentPathUnmodifiable, listener, commentListener, dryRun);
                    if (count > 0 && dryRun)
                        return count;
                }
                else if (dryRun)
                {
                    return 1;
                }
                else
                {
                    CommentedConfig newValue = config.createSubConfig();
                    configMap.put(key, newValue);
                    listener.onCorrect(action, parentPathUnmodifiable, configValue, newValue);
                    count++;
                    count += correct((Config)specValue, newValue, parentPath, parentPathUnmodifiable, listener, commentListener, dryRun);
                }

                String newComment = levelComments.get(parentPath);
                String oldComment = config.getComment(key);
                if (!stringsMatchIgnoringNewlines(oldComment, newComment))
                {
                    if(commentListener != null)
                        commentListener.onCorrect(action, parentPathUnmodifiable, oldComment, newComment);

                    if (dryRun)
                        return 1;

                    config.setComment(key, newComment);
                }
            }
            else
            {
                ValueSpec valueSpec = (ValueSpec)specValue;
                if (!valueSpec.test(configValue))
                {
                    if (dryRun)
                        return 1;

                    Object newValue = valueSpec.correct(configValue);
                    configMap.put(key, newValue);
                    listener.onCorrect(action, parentPathUnmodifiable, configValue, newValue);
                    count++;
                }
                String oldComment = config.getComment(key);
                if (!stringsMatchIgnoringNewlines(oldComment, valueSpec.getComment()))
                {
                    if (commentListener != null)
                        commentListener.onCorrect(action, parentPathUnmodifiable, oldComment, valueSpec.getComment());

                    if (dryRun)
                        return 1;

                    config.setComment(key, valueSpec.getComment());
                }
            }

            parentPath.removeLast();
        }

        // Second step: removes the unspecified values
        for (Iterator<Map.Entry<String, Object>> ittr = configMap.entrySet().iterator(); ittr.hasNext();)
        {
            Map.Entry<String, Object> entry = ittr.next();
            if (!specMap.containsKey(entry.getKey()))
            {
                if (dryRun)
                    return 1;

                ittr.remove();
                parentPath.addLast(entry.getKey());
                listener.onCorrect(REMOVE, parentPathUnmodifiable, entry.getValue(), null);
                parentPath.removeLast();
                count++;
            }
        }
        return count;
    }

    private boolean stringsMatchIgnoringNewlines(@Nullable Object obj1, @Nullable Object obj2)
    {
        if(obj1 instanceof String && obj2 instanceof String)
        {
            String string1 = (String) obj1;
            String string2 = (String) obj2;

            if(string1.length() > 0 && string2.length() > 0)
            {
                return string1.replaceAll("\r\n", "\n")
                    .equals(string2.replaceAll("\r\n", "\n"));

            }
        }
        // Fallback for when we're not given Strings, or one of them is empty
        return Objects.equals(obj1, obj2);
    }

    public static class Builder
    {
        private final Config storage = Config.of(LinkedHashMap::new, InMemoryFormat.withUniversalSupport()); // Use LinkedHashMap for consistent ordering
        private BuilderContext context = new BuilderContext();
        private final Map<List<String>, String> levelComments = new HashMap<>();
        private final List<String> currentPath = new ArrayList<>();
        public List<ConfigValue<?>> values = new ArrayList<>();

        //Object
        public <T> ConfigValue<T> define(String path, T defaultValue) {
            return define(split(path), defaultValue);
        }
        public <T> ConfigValue<T> define(List<String> path, T defaultValue) {
            return define(path, defaultValue, o -> o != null && defaultValue.getClass().isAssignableFrom(o.getClass()));
        }
        public <T> ConfigValue<T> define(String path, T defaultValue, Predicate<Object> validator) {
            return define(split(path), defaultValue, validator);
        }
        public <T> ConfigValue<T> define(List<String> path, T defaultValue, Predicate<Object> validator) {
            Objects.requireNonNull(defaultValue, "Default value can not be null");
            return define(path, () -> defaultValue, validator);
        }
        public <T> ConfigValue<T> define(String path, Supplier<T> defaultSupplier, Predicate<Object> validator) {
            return define(split(path), defaultSupplier, validator);
        }
        public <T> ConfigValue<T> define(List<String> path, Supplier<T> defaultSupplier, Predicate<Object> validator) {
            return define(path, defaultSupplier, validator, Object.class);
        }
        public <T> ConfigValue<T> define(List<String> path, Supplier<T> defaultSupplier, Predicate<Object> validator, Class<?> clazz) {
            context.setClazz(clazz);
            return define(path, new ValueSpec(defaultSupplier, validator, context), defaultSupplier);
        }
        public <T> ConfigValue<T> define(List<String> path, ValueSpec value, Supplier<T> defaultSupplier) { // This is the root where everything at the end of the day ends up.
            if (!currentPath.isEmpty()) {
                List<String> tmp = new ArrayList<>(currentPath.size() + path.size());
                tmp.addAll(currentPath);
                tmp.addAll(path);
                path = tmp;
            }
            storage.set(path, value);
            context = new BuilderContext();
            return new ConfigValue<>(this, path, defaultSupplier);
        }
        public <V extends Comparable<? super V>> ConfigValue<V> defineInRange(String path, V defaultValue, V min, V max, Class<V> clazz) {
            return defineInRange(split(path), defaultValue, min, max, clazz);
        }
        public <V extends Comparable<? super V>> ConfigValue<V> defineInRange(List<String> path,  V defaultValue, V min, V max, Class<V> clazz) {
            return defineInRange(path, (Supplier<V>)() -> defaultValue, min, max, clazz);
        }
        public <V extends Comparable<? super V>> ConfigValue<V> defineInRange(String path, Supplier<V> defaultSupplier, V min, V max, Class<V> clazz) {
            return defineInRange(split(path), defaultSupplier, min, max, clazz);
        }
        public <V extends Comparable<? super V>> ConfigValue<V> defineInRange(List<String> path, Supplier<V> defaultSupplier, V min, V max, Class<V> clazz) {
            Range<V> range = new Range<>(clazz, min, max);
            context.setRange(range);
            context.setComment(ObjectArrays.concat(context.getComment(), "Range: " + range.toString()));
            if (min.compareTo(max) > 0)
                throw new IllegalArgumentException("Range min most be less then max.");
            return define(path, defaultSupplier, range);
        }
        public <T> ConfigValue<T> defineInList(String path, T defaultValue, Collection<? extends T> acceptableValues) {
            return defineInList(split(path), defaultValue, acceptableValues);
        }
        public <T> ConfigValue<T> defineInList(String path, Supplier<T> defaultSupplier, Collection<? extends T> acceptableValues) {
            return defineInList(split(path), defaultSupplier, acceptableValues);
        }
        public <T> ConfigValue<T> defineInList(List<String> path, T defaultValue, Collection<? extends T> acceptableValues) {
            return defineInList(path, () -> defaultValue, acceptableValues);
        }
        public <T> ConfigValue<T> defineInList(List<String> path, Supplier<T> defaultSupplier, Collection<? extends T> acceptableValues) {
            return define(path, defaultSupplier, acceptableValues::contains);
        }
        public <T> ConfigValue<List<? extends T>> defineList(String path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
            return defineList(split(path), defaultValue, elementValidator);
        }
        public <T> ConfigValue<List<? extends T>> defineList(String path, Supplier<List<? extends T>> defaultSupplier, Predicate<Object> elementValidator) {
            return defineList(split(path), defaultSupplier, elementValidator);
        }
        public <T> ConfigValue<List<? extends T>> defineList(List<String> path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
            return defineList(path, () -> defaultValue, elementValidator);
        }
        public <T> ConfigValue<List<? extends T>> defineList(List<String> path, Supplier<List<? extends T>> defaultSupplier, Predicate<Object> elementValidator) {
            context.setClazz(List.class);
            return define(path, new ValueSpec(defaultSupplier, x -> x instanceof List && ((List<?>) x).stream().allMatch( elementValidator ), context) {
                @Override
                public Object correct(Object value) {
                    if (!(value instanceof List) || ((List<?>) value).isEmpty()) {
                        LogManager.getLogger().debug(CORE, "List on key {} is deemed to need correction. It is null, not a list, or an empty list. Modders, consider defineListAllowEmpty?", path.get(path.size() - 1));
                        return getDefault();
                    }
                    List<?> list = Lists.newArrayList((List<?>) value);
                    list.removeIf(elementValidator.negate());
                    if (list.isEmpty()) {
                        LogManager.getLogger().debug(CORE, "List on key {} is deemed to need correction. It failed validation.", path.get(path.size() - 1));
                        return getDefault();
                    }
                    return list;
                }
            }, defaultSupplier);
        }

        public <T> ConfigValue<List<? extends T>> defineListAllowEmpty(List<String> path, Supplier<List<? extends T>> defaultSupplier, Predicate<Object> elementValidator) {
            context.setClazz(List.class);
            return define(path, new ValueSpec(defaultSupplier, x -> x instanceof List && ((List<?>) x).stream().allMatch( elementValidator ), context) {
                @Override
                public Object correct(Object value) {
                    if (!(value instanceof List)) {
                        LogManager.getLogger().debug(CORE, "List on key {} is deemed to need correction, as it is null or not a list.", path.get(path.size() - 1));
                        return getDefault();
                    }
                    List<?> list = Lists.newArrayList((List<?>) value);
                    list.removeIf(elementValidator.negate());
                    if (list.isEmpty()) {
                        LogManager.getLogger().debug(CORE, "List on key {} is deemed to need correction. It failed validation.", path.get(path.size() - 1));
                        return getDefault();
                    }
                    return list;
                }
            }, defaultSupplier);
        }

        //Enum
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue) {
            return defineEnum(split(path), defaultValue);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter) {
            return defineEnum(split(path), defaultValue, converter);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue) {
            return defineEnum(path, defaultValue, defaultValue.getDeclaringClass().getEnumConstants());
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter) {
            return defineEnum(path, defaultValue, converter, defaultValue.getDeclaringClass().getEnumConstants());
        }
        @SafeVarargs
        public final <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue, V... acceptableValues) {
            return defineEnum(split(path), defaultValue, acceptableValues);
        }
        @SafeVarargs
        public final <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter, V... acceptableValues) {
            return defineEnum(split(path), defaultValue, converter, acceptableValues);
        }
        @SafeVarargs
        public final <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue, V... acceptableValues) {
            return defineEnum(path, defaultValue, Arrays.asList(acceptableValues));
        }
        @SafeVarargs
        public final <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter, V... acceptableValues) {
            return defineEnum(path, defaultValue, converter, Arrays.asList(acceptableValues));
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue, Collection<V> acceptableValues) {
            return defineEnum(split(path), defaultValue, acceptableValues);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter, Collection<V> acceptableValues) {
            return defineEnum(split(path), defaultValue, converter, acceptableValues);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue, Collection<V> acceptableValues) {
            return defineEnum(path, defaultValue, EnumGetMethod.NAME_IGNORECASE, acceptableValues);
        }
        @SuppressWarnings("boxing")
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter, Collection<V> acceptableValues) {
            return defineEnum(path, defaultValue, converter, obj -> {
                if (obj instanceof Enum) {
                    return acceptableValues.contains(obj);
                }
                if (obj == null) {
                    return false;
                }
                try {
                    return acceptableValues.contains(converter.get(obj, defaultValue.getDeclaringClass()));
                } catch (IllegalArgumentException | ClassCastException e) {
                    return false;
                }
            });
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue, Predicate<Object> validator) {
            return defineEnum(split(path), defaultValue, validator);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter, Predicate<Object> validator) {
            return defineEnum(split(path), defaultValue, converter, validator);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue, Predicate<Object> validator) {
            return defineEnum(path, () -> defaultValue, validator, defaultValue.getDeclaringClass());
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter, Predicate<Object> validator) {
            return defineEnum(path, () -> defaultValue, converter, validator, defaultValue.getDeclaringClass());
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, Supplier<V> defaultSupplier, Predicate<Object> validator, Class<V> clazz) {
            return defineEnum(split(path), defaultSupplier, validator, clazz);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(String path, Supplier<V> defaultSupplier, EnumGetMethod converter, Predicate<Object> validator, Class<V> clazz) {
            return defineEnum(split(path), defaultSupplier, converter, validator, clazz);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, Supplier<V> defaultSupplier, Predicate<Object> validator, Class<V> clazz) {
            return defineEnum(path, defaultSupplier, EnumGetMethod.NAME_IGNORECASE, validator, clazz);
        }
        public <V extends Enum<V>> EnumValue<V> defineEnum(List<String> path, Supplier<V> defaultSupplier, EnumGetMethod converter, Predicate<Object> validator, Class<V> clazz) {
            context.setClazz(clazz);
            V[] allowedValues = clazz.getEnumConstants();
            context.setComment(ObjectArrays.concat(context.getComment(), "Allowed Values: " + Arrays.stream(allowedValues).filter(validator).map(Enum::name).collect(Collectors.joining(", "))));
            return new EnumValue<>(this, define(path, new ValueSpec(defaultSupplier, validator, context), defaultSupplier).getPath(), defaultSupplier, converter, clazz);
        }

        //boolean
        public BooleanValue define(String path, boolean defaultValue) {
            return define(split(path), defaultValue);
        }
        public BooleanValue define(List<String> path, boolean defaultValue) {
            return define(path, () -> defaultValue);
        }
        public BooleanValue define(String path, Supplier<Boolean> defaultSupplier) {
            return define(split(path), defaultSupplier);
        }
        public BooleanValue define(List<String> path, Supplier<Boolean> defaultSupplier) {
            return new BooleanValue(this, define(path, defaultSupplier, o -> {
                if (o instanceof String) return ((String)o).equalsIgnoreCase("true") || ((String)o).equalsIgnoreCase("false");
                return o instanceof Boolean;
            }, Boolean.class).getPath(), defaultSupplier);
        }

        //Double
        public DoubleValue defineInRange(String path, double defaultValue, double min, double max) {
            return defineInRange(split(path), defaultValue, min, max);
        }
        public DoubleValue defineInRange(List<String> path, double defaultValue, double min, double max) {
            return defineInRange(path, () -> defaultValue, min, max);
        }
        public DoubleValue defineInRange(String path, Supplier<Double> defaultSupplier, double min, double max) {
            return defineInRange(split(path), defaultSupplier, min, max);
        }
        public DoubleValue defineInRange(List<String> path, Supplier<Double> defaultSupplier, double min, double max) {
            return new DoubleValue(this, defineInRange(path, defaultSupplier, min, max, Double.class).getPath(), defaultSupplier);
        }

        //Ints
        public IntValue defineInRange(String path, int defaultValue, int min, int max) {
            return defineInRange(split(path), defaultValue, min, max);
        }
        public IntValue defineInRange(List<String> path, int defaultValue, int min, int max) {
            return defineInRange(path, () -> defaultValue, min, max);
        }
        public IntValue defineInRange(String path, Supplier<Integer> defaultSupplier, int min, int max) {
            return defineInRange(split(path), defaultSupplier, min, max);
        }
        public IntValue defineInRange(List<String> path, Supplier<Integer> defaultSupplier, int min, int max) {
            return new IntValue(this, defineInRange(path, defaultSupplier, min, max, Integer.class).getPath(), defaultSupplier);
        }

        //Longs
        public LongValue defineInRange(String path, long defaultValue, long min, long max) {
            return defineInRange(split(path), defaultValue, min, max);
        }
        public LongValue defineInRange(List<String> path, long defaultValue, long min, long max) {
            return defineInRange(path, () -> defaultValue, min, max);
        }
        public LongValue defineInRange(String path, Supplier<Long> defaultSupplier, long min, long max) {
            return defineInRange(split(path), defaultSupplier, min, max);
        }
        public LongValue defineInRange(List<String> path, Supplier<Long> defaultSupplier, long min, long max) {
            return new LongValue(this, defineInRange(path, defaultSupplier, min, max, Long.class).getPath(), defaultSupplier);
        }

        public Builder comment(String comment)
        {
            if(comment == null || comment.isEmpty()) comment = "No comment";
            context.setComment(comment);
            return this;
        }
        public Builder comment(String... comment)
        {
            if(comment == null || comment.length < 1 || (comment.length == 1 && comment[0].isEmpty()))
            {
                comment = new String[] {"No comment"};
            }

            context.setComment(comment);
            return this;
        }

        public Builder translation(String translationKey)
        {
            context.setTranslationKey(translationKey);
            return this;
        }

        public Builder worldRestart()
        {
            context.worldRestart();
            return this;
        }

        public Builder push(String path) {
            return push(split(path));
        }

        public Builder push(List<String> path) {
            currentPath.addAll(path);
            if (context.hasComment()) {

                levelComments.put(new ArrayList<>(currentPath), context.buildComment());
                context.setComment(); // Set to empty
            }
            context.ensureEmpty();
            return this;
        }

        public Builder pop() {
            return pop(1);
        }

        public Builder pop(int count) {
            if (count > currentPath.size())
                throw new IllegalArgumentException("Attempted to pop " + count + " elements when we only had: " + currentPath);
            for (int x = 0; x < count; x++)
                currentPath.remove(currentPath.size() - 1);
            return this;
        }

        public <T> Pair<T, TomlConfig> configure(Function<Builder, T> consumer) {
            T o = consumer.apply(this);
            return Pair.of(o, this.build());
        }

        public TomlConfig build()
        {
            context.ensureEmpty();
            Config valueCfg = Config.of(Config.getDefaultMapCreator(true, true), InMemoryFormat.withSupport(ConfigValue.class::isAssignableFrom));
            values.forEach(v -> valueCfg.set(v.getPath(), v));

            TomlConfig ret = new TomlConfig(storage, valueCfg, levelComments);
            values.forEach(v -> v.spec = ret);
            return ret;
        }

        public interface BuilderConsumer {
            void accept(Builder builder);
        }
    }

    public static class BuilderContext
    {
        private @Nonnull String[] comment = new String[0];
        private String langKey;
        private Range<?> range;
        private boolean worldRestart = false;
        private Class<?> clazz;

        public void setComment(String... value) { this.comment = value == null ? new String[] {""} : value; } // TODO 1.17: this should throw an IllegalStateException in future
        public boolean hasComment() { return this.comment.length > 0; }
        public String[] getComment() { return this.comment; }
        public String buildComment() { return LINE_JOINER.join(comment); }
        public void setTranslationKey(String value) { this.langKey = value; }
        public String getTranslationKey() { return this.langKey; }
        public <V extends Comparable<? super V>> void setRange(Range<V> value)
        {
            this.range = value;
            this.setClazz(value.getClazz());
        }
        @SuppressWarnings("unchecked")
        public <V extends Comparable<? super V>> Range<V> getRange() { return (Range<V>)this.range; }
        public void worldRestart() { this.worldRestart = true; }
        public boolean needsWorldRestart() { return this.worldRestart; }
        public void setClazz(Class<?> clazz) { this.clazz = clazz; }
        public Class<?> getClazz(){ return this.clazz; }

        public void ensureEmpty()
        {
            validate(hasComment(), "Non-empty comment when empty expected");
            validate(langKey, "Non-null translation key when null expected");
            validate(range, "Non-null range when null expected");
            validate(worldRestart, "Dangeling world restart value set to true");
        }

        private void validate(Object value, String message)
        {
            if (value != null)
                throw new IllegalStateException(message);
        }
        private void validate(boolean value, String message)
        {
            if (value)
                throw new IllegalStateException(message);
        }
    }

    private static final Joiner LINE_JOINER = Joiner.on("\n");
    private static final Joiner DOT_JOINER = Joiner.on(".");
    private static final Splitter DOT_SPLITTER = Splitter.on(".");
    private static List<String> split(String path)
    {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }
}
