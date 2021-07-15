package mekanism.common.tile.machine;

import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TileEntityDigitalMiner extends TileEntityMekanism implements IBoundingBlock {

    public TileEntityDigitalMiner(BlockPos pos, BlockState state) {
        super(MekanismBlocks.DIGITAL_MINER, pos, state);
    }

    @Override
    public void onPlace() {
        super.onPlace();
        if (world != null) {
            BlockPos pos = getPos();
            for (int x = -1; x <= +1; x++) {
                for (int y = 0; y <= +1; y++) {
                    for (int z = -1; z <= +1; z++) {
                        if (x != 0 || y != 0 || z != 0) {
                            BlockPos boundingPos = pos.add(x, y, z);
                            WorldUtils.makeAdvancedBoundingBlock(world, boundingPos, pos);
                            world.updateNeighbors(boundingPos, getBlockType());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (world != null) {
            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x != 0 || y != 0 || z != 0) {
                            world.removeBlock(getPos().add(x, y, z), false);
                        }
                    }
                }
            }
        }
    }

}
