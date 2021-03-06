package mekanism.api.providers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface IItemProvider extends IBaseProvider, ItemConvertible {

    /**
     * Gets the item this provider represents.
     */
    @NotNull
    Item getItem();//TODO - 1.17: Replace this with just using vanilla's asItem?

    @NotNull
    @Override
    default Item asItem() {
        return getItem();
    }

    /**
     * Creates an item stack of size one using the item this provider represents.
     */
    @NotNull
    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    /**
     * Creates an item stack of the given size using the item this provider represents.
     *
     * @param size Size of the stack.
     */
    @NotNull
    default ItemStack getItemStack(int size) {
        return new ItemStack(getItem(), size);
    }

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean itemMatches(ItemStack otherStack) {
        return itemMatches(otherStack.getItem());
    }

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean itemMatches(Item other) {
        return getItem() == other;
    }

    @Override
    default Identifier getRegistryName() {
        return Registry.ITEM.getId(getItem());
    }

    @Override
    default String getTranslationKey() {
        return getItem().getTranslationKey();
    }
}

