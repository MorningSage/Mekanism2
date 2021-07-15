package mekanism.common.item.block.machine;

import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.ItemDeferredRegister;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ItemBlockMachine extends ItemBlockTooltip<BlockTile<?, ?>> {

    public ItemBlockMachine(BlockTile<?, ?> block) {
        super(block, true, ItemDeferredRegister.getMekBaseProperties().maxCount(1));
    }

    @Override
    public void addDetails(@NotNull ItemStack stack, World world, @NotNull List<Text> tooltip, boolean advanced) {

    }

}

