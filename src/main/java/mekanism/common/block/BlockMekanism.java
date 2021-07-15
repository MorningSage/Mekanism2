package mekanism.common.block;

import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Special handling for block drops that need TileEntity data
 */
public abstract class BlockMekanism extends Block implements BlockEntityProvider {

    protected BlockMekanism(AbstractBlock.Settings properties) {
        super(BlockStateHelper.applyLightLevelAdjustments(properties));
        setDefaultState(BlockStateHelper.getDefaultState(getStateManager().getDefaultState()));
    }

    @NotNull
    @Override
    @Deprecated
    public PistonBehavior getPistonBehavior(@NotNull BlockState state) {
        if (hasTileEntity(state)) {
            //Protect against mods like Quark that allow blocks with TEs to be moved
            //TODO: Eventually it would be nice to go through this and maybe even allow some TEs to be moved if they don't strongly
            // care about the world, but for now it is safer to just block them from being moved
            return PistonBehavior.BLOCK;
        }
        return super.getPistonBehavior(state);
    }

    @Override
    public void onPlaced(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);

        TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);

        if (tile == null) return;
        tile.onPlace();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (this instanceof IHasTileEntity) {
            return ((IHasTileEntity<?>) this).getTileType().instantiate(pos, state);
        }
        return null;
    }

    //@Override
    public boolean hasTileEntity(BlockState state) {
        return this instanceof IHasTileEntity;
    }

    @Override
    protected void appendProperties(@NotNull StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        BlockStateHelper.fillBlockStateContainer(this, builder);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        return BlockStateHelper.getStateForPlacement(this, super.getPlacementState(context), context);
    }
}

