package mekanism.api.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;

/**
 * NO-OP IInventory
 */
@MethodsReturnNonnullByDefault
public final class IgnoredIInventory implements Inventory {

    public static final IgnoredIInventory INSTANCE = new IgnoredIInventory();

    private IgnoredIInventory() {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getStack(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int index, @Nonnull ItemStack stack) {
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(@Nonnull PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
    }
}
