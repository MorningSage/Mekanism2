package mekanism.common.config.value;

/*
 * This class is based on the MinecraftForge's implementation of
 * their Night-Config wrapper.  This class only is licensed under
 * the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation version 2.1 of the License.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Predicate;

public class Range<V extends Comparable<? super V>> implements Predicate<Object> {
    public static final Marker CORE = MarkerManager.getMarker("CORE");

    private final Class<? extends V> clazz;
    private final V min;
    private final V max;

    public Range(Class<V> clazz, V min, V max) {
        this.clazz = clazz;
        this.min = min;
        this.max = max;
    }

    public Class<? extends V> getClazz() { return clazz; }
    public V getMin() { return min; }
    public V getMax() { return max; }

    private boolean isNumber(Object other) {
        return Number.class.isAssignableFrom(clazz) && other instanceof Number;
    }

    @Override
    public boolean test(Object t) {
        if (isNumber(t)) {
            Number n = (Number) t;
            boolean result = ((Number)min).doubleValue() <= n.doubleValue() && n.doubleValue() <= ((Number)max).doubleValue();
            if (!result) {
                LogManager.getLogger().debug(CORE, "Range value {} is not within its bounds {}-{}", n.doubleValue(), ((Number)min).doubleValue(), ((Number)max).doubleValue());
            }
            return result;
        }

        if (!clazz.isInstance(t)) return false;
        V c = clazz.cast(t);

        boolean result = c.compareTo(min) >= 0 && c.compareTo(max) <= 0;
        if(!result) {
            LogManager.getLogger().debug(CORE, "Range value {} is not within its bounds {}-{}", c, min, max);
        }
        return result;
    }

    public Object correct(Object value, Object def) {
        if (isNumber(value)) {
            Number n = (Number) value;
            return n.doubleValue() < ((Number)min).doubleValue() ? min : n.doubleValue() > ((Number)max).doubleValue() ? max : value;
        }

        if (!clazz.isInstance(value)) return def;
        V c = clazz.cast(value);
        return c.compareTo(min) < 0 ? min : c.compareTo(max) > 0 ? max : value;
    }

    @Override
    public String toString() {
        if (clazz == Integer.class) {
            if (max.equals(Integer.MAX_VALUE)) {
                return "> " + min;
            } else if (min.equals(Integer.MIN_VALUE)) {
                return "< " + max;
            }
        }
        return min + " ~ " + max;
    }
}
