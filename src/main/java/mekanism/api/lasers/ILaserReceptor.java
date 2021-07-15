package mekanism.api.lasers;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mekanism.api.math.FloatingLong;
import net.minecraft.util.math.Direction;

import javax.annotation.Nonnull;

/**
 * Expose this as a capability on your TileEntity to handle what happens when a laser hits it.
 */
public interface ILaserReceptor extends ComponentV3, AutoSyncedComponent {

    /**
     * Called to receive energy from a laser when the block is hit by a laser.
     *
     * @param energy Energy received.
     * @param side   Side the receptor is hit from (will be removed in 1.17 as what capability instance is returned can be used to have different handling of this)
     */
    void receiveLaserEnergy(@Nonnull FloatingLong energy, Direction side);//TODO - 1.17: Remove the side from this as the side is used when getting the capability.

    /**
     * Checks if a laser can break this receptor.
     *
     * @return {@code false} to not allow the laser to break this block.
     */
    boolean canLasersDig();
}
