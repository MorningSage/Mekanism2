package mekanism.api.util;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class StackUtils {
    private StackUtils() {
    }

    public static boolean canItemStacksStack(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || !a.isItemEqual(b) || a.hasTag() != b.hasTag()) return false;

        return (!a.hasTag() || a.getTag().equals(b.getTag()));
    }
}
