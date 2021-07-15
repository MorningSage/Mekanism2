package mekanism.common.item.block.machine;

import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.tile.machine.TileEntityDigitalMiner;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemBlockDigitalMiner extends ItemBlockMachine {

    public ItemBlockDigitalMiner(BlockTile<TileEntityDigitalMiner, Machine<TileEntityDigitalMiner>> block) {
        super(block);
    }

    @Override
    public boolean place(@NotNull ItemPlacementContext context, @NotNull BlockState state) {
        World world = context.getWorld();
        BlockPos placePos = context.getBlockPos();
        for (int xPos = -1; xPos <= 1; xPos++) {
            for (int yPos = 0; yPos <= 1; yPos++) {
                for (int zPos = -1; zPos <= 1; zPos++) {
                    BlockPos pos = placePos.add(xPos, yPos, zPos);
                    if (!WorldUtils.isValidReplaceableBlock(world, pos)) {
                        // If it won't fit then fail
                        return false;
                    }
                }
            }
        }
        return super.place(context, state);
    }
}

