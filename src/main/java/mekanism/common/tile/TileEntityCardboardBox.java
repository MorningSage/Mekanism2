package mekanism.common.tile;

import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tile.base.TileEntityUpdateable;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TileEntityCardboardBox extends TileEntityUpdateable {

    public TileEntityCardboardBox(BlockPos pos, BlockState state) {
        super(MekanismTileEntityTypes.CARDBOARD_BOX.getTileEntityType(), pos, state);
    }

}
