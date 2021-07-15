package mekanism.common.tile.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.Optional;

/**
 * Internal interface.  A bounding block is not actually a 'bounding' block, it is really just a fake block that is used to mimic actual block bounds.
 *
 * @author AidanBrady
 */
public interface IBoundingBlock {
    /**
     * Called when the main block is placed.
     */
    //TODO: Evaluate this, onPlace is a method in TileEntityMekanism now, so it always gets called via that exposure
    // So having this here may not be needed anymore
    void onPlace();

}

