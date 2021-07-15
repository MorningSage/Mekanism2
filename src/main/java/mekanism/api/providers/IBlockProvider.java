package mekanism.api.providers;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface IBlockProvider extends IItemProvider {

    @NotNull
    Block getBlock();

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean blockMatches(ItemStack otherStack) {
        Item item = otherStack.getItem();
        return item instanceof BlockItem && blockMatches(((BlockItem) item).getBlock());
    }

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean blockMatches(Block other) {
        return getBlock() == other;
    }

    @Override
    default Identifier getRegistryName() {
        return Registry.BLOCK.getId(getBlock());
    }

    @Override
    default String getTranslationKey() {
        return getBlock().getTranslationKey();
    }
}

