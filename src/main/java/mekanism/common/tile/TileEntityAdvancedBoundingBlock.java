package mekanism.common.tile;

import mekanism.common.registries.MekanismTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

//TODO - 1.17: Remove advanced bounding blocks
public class TileEntityAdvancedBoundingBlock extends TileEntityBoundingBlock {

    public TileEntityAdvancedBoundingBlock(BlockPos pos, BlockState state) {
        super(MekanismTileEntityTypes.ADVANCED_BOUNDING_BLOCK.getTileEntityType(), pos, state);
    }
}
