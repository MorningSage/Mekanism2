package mekanism.api;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

/**
 * Expose this as a capability on your TileEntity to allow if your block to be modified by a Configurator.
 *
 * @author aidancbrady
 */
public interface IConfigurable extends ComponentV3, AutoSyncedComponent {//TODO - 1.17: Make the capability instance handle what side we interact with these from and remove the side from the methods

    /**
     * Called when a player shift-right clicks this block with a Configurator.
     *
     * @param player - the player who clicked the block
     * @param side   - the side the block was clicked on
     *
     * @return action that was performed
     *
     * @apiNote Only called from the server
     */
    ActionResult onSneakRightClick(PlayerEntity player, Direction side);

    /**
     * Called when a player right clicks this block with a Configurator.
     *
     * @param player - the player who clicked the block
     * @param side   - the side the block was clicked on
     *
     * @return action that was performed
     *
     * @apiNote Only called from the server
     */
    ActionResult onRightClick(PlayerEntity player, Direction side);
}
