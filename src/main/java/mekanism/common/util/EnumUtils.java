package mekanism.common.util;

import net.minecraft.util.math.Direction;

public class EnumUtils {

    private EnumUtils() {
    }

    /**
     * Cached value of {@link Direction#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final Direction[] DIRECTIONS = Direction.values();

    /**
     * Cached value of the horizontal directions. DO NOT MODIFY THIS LIST.
     *
     * @implNote Index is ordinal() - 2, as the first two elements of {@link Direction} are {@link Direction#DOWN} and {@link Direction#UP}
     */
    public static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};


}
