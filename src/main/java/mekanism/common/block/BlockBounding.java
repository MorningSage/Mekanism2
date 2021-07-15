package mekanism.common.block;

import mekanism.common.Mekanism;
import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tile.TileEntityBoundingBlock;
import mekanism.common.util.AccessorUtils;
import mekanism.common.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO: Extend MekanismBlock. Not done yet as checking is needed to ensure how drops happen still happens correctly and things in the super class don't mess this up
public class BlockBounding extends Block implements IHasTileEntity<TileEntityBoundingBlock>, BlockEntityProvider {

    @Nullable
    public static BlockPos getMainBlockPos(BlockView world, BlockPos thisPos) {
        TileEntityBoundingBlock te = WorldUtils.getTileEntity(TileEntityBoundingBlock.class, world, thisPos);
        if (te != null && /*te.receivedCoords &&*/ !thisPos.equals(te.getMainPos())) {
            return te.getMainPos();
        }
        return null;
    }

    //Note: This is not a block state so that we can have it easily create the correct TileEntity.
    // If we end up merging some logic from the TileEntities then this can become a property
    private final boolean advanced;

    public BlockBounding(boolean advanced) {
        //Note: We require setting variable opacity so that the block state does not cache the ability of if blocks can be placed on top of the bounding block
        // Torches cannot be placed on the sides due to vanilla checking the incorrect shape
        super(BlockStateHelper.applyLightLevelAdjustments(AbstractBlock.Settings.of(Material.METAL).strength(3.5F, 4.8F)
            .requiresTool().dynamicBounds()));
        this.advanced = advanced;
        setDefaultState(BlockStateHelper.getDefaultState(stateManager.getDefaultState()));
    }

    @Override
    protected void appendProperties(@NotNull StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        BlockStateHelper.fillBlockStateContainer(this, builder);
    }

    @NotNull
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public PistonBehavior getPistonBehavior(@NotNull BlockState state) {
        //Protect against mods like Quark that allow blocks with TEs to be moved
        return PistonBehavior.BLOCK;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        return BlockStateHelper.getStateForPlacement(this, super.getPlacementState(context), context);
    }

    @NotNull
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos == null) {
            return ActionResult.FAIL;
        }
        BlockState mainState = world.getBlockState(mainPos);
        //TODO: Use proper ray trace result, currently is using the one we got but we probably should make one with correct position information
        return mainState.getBlock().onUse(mainState, world, mainPos, player, hand, hit);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        //Remove the main block if a bounding block gets broken by being directly replaced
        // Note: We only do this if we don't go from bounding block to bounding block
        if (!state.isOf(newState.getBlock())) {
            BlockPos mainPos = getMainBlockPos(world, pos);
            if (mainPos != null) {
                BlockState mainState = world.getBlockState(mainPos);
                if (!mainState.isAir()) {
                    //Set the main block to air, which will invalidate the rest of the bounding blocks
                    world.removeBlock(mainPos, false);
                }
            }
            super.onStateReplaced(state, world, pos, newState, isMoving);
        }
    }

    //@Override
    //public boolean removedByPlayer(@NotNull BlockState state, World world, @NotNull BlockPos pos, @NotNull PlayerEntity player, boolean willHarvest, FluidState fluidState) {
    //    if (willHarvest) {
    //        return true;
    //    }
    //    BlockPos mainPos = getMainBlockPos(world, pos);
    //    if (mainPos != null) {
    //        BlockState mainState = world.getBlockState(mainPos);
    //        if (!mainState.isAir()) {
    //            //Set the main block to air, which will invalidate the rest of the bounding blocks
    //            mainState.removedByPlayer(world, mainPos, player, false, mainState.getFluidState());
    //        }
    //    }
    //    return super.removedByPlayer(state, world, pos, player, false, fluidState);
    //}

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos != null) {
            BlockState mainState = world.getBlockState(mainPos);
            if (!mainState.isAir()) {
                //Proxy the explosion to the main block which, will set it to air causing it to invalidate the rest of the bounding blocks
                LootContext.Builder lootContextBuilder = new LootContext.Builder((ServerWorld) world)
                    .random(world.random)
                    .parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(mainPos))
                    .parameter(LootContextParameters.TOOL, ItemStack.EMPTY)
                    .optionalParameter(LootContextParameters.BLOCK_ENTITY, mainState.hasBlockEntity() ? WorldUtils.getTileEntity(world, mainPos) : null)
                    .optionalParameter(LootContextParameters.THIS_ENTITY, explosion.getCausingEntity());
                if (AccessorUtils.getDestructionType(explosion) == Explosion.DestructionType.DESTROY) {
                    lootContextBuilder.parameter(LootContextParameters.EXPLOSION_RADIUS, AccessorUtils.getPower(explosion));
                }
                mainState.getDroppedStacks(lootContextBuilder).forEach(stack -> Block.dropStack(world, mainPos, stack));
                mainState.getBlock().onDestroyedByExplosion(world, mainPos, explosion);
            }
        }
        super.onDestroyedByExplosion(world, pos, explosion);
    }

    @Override
    public void afterBreak(@NotNull World world, @NotNull PlayerEntity player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity te, @NotNull ItemStack stack) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos != null) {
            BlockState mainState = world.getBlockState(mainPos);
            mainState.getBlock().afterBreak(world, player, mainPos, mainState, WorldUtils.getTileEntity(world, mainPos), stack);
        } else {
            super.afterBreak(world, player, pos, state, te, stack);
        }
        world.removeBlock(pos, false);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void neighborUpdate(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean isMoving) {
        if (!world.isClient) {
            TileEntityBoundingBlock tile = WorldUtils.getTileEntity(TileEntityBoundingBlock.class, world, pos);
            if (tile != null) {
                //tile.onNeighborChange(neighborBlock, neighborPos);
            }
        }
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos != null) {
            world.getBlockState(mainPos).neighborUpdate(world, mainPos, neighborBlock, neighborPos, isMoving);
        }
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public boolean hasComparatorOutput(@NotNull BlockState blockState) {
        //TODO: Figure out if there is a better way to do this so it doesn't have to return true for all bounding blocks
        return true;
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public float calcBlockBreakingDelta(@NotNull BlockState state, @NotNull PlayerEntity player, @NotNull BlockView world, @NotNull BlockPos pos) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos == null) {
            return super.calcBlockBreakingDelta(state, player, world, pos);
        }
        return world.getBlockState(mainPos).calcBlockBreakingDelta(player, world, mainPos);
    }

    @NotNull
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    //@Override
    //public boolean hasTileEntity(BlockState state) {
    //    return true;
    //}

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return getTileType().instantiate(pos, state);
    }

    //@Override
    //public BlockEntity createTileEntity(@NotNull BlockState state, @NotNull BlockView world) {
    //    return getTileType().create();
    //}

    @Override
    public BlockEntityType<TileEntityBoundingBlock> getTileType() {
        if (advanced) {
            return MekanismTileEntityTypes.ADVANCED_BOUNDING_BLOCK.getTileEntityType();
        }
        return MekanismTileEntityTypes.BOUNDING_BLOCK.getTileEntityType();
    }

    @NotNull
    @Override
    @Deprecated
    public VoxelShape getOutlineShape(@NotNull BlockState state, @NotNull BlockView world, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos == null) {
            //If we don't have a main pos, then act as if the block is empty so that we can move into it properly
            return VoxelShapes.empty();
        }
        BlockState mainState;
        try {
            mainState = world.getBlockState(mainPos);
        } catch (ArrayIndexOutOfBoundsException e) {
            //Note: ChunkRenderCache is client side only, though it does not seem to have any class loading issues on the server
            // due to this exception not being caught in that specific case
            if (world instanceof ChunkRendererRegion) {
                //Workaround for when the main spot of the miner is out of bounds of the ChunkRenderCache thus causing an
                // ArrayIndexOutOfBoundException on the client as seen by:
                // https://github.com/mekanism/Mekanism/issues/5792
                // https://github.com/mekanism/Mekanism/issues/5844
                world = AccessorUtils.getWorld((ChunkRendererRegion) world);
                mainState = world.getBlockState(mainPos);
            } else {
                Mekanism.logger.error("Error getting bounding block shape, for position {}, with main position {}. World of type {}", pos, mainPos, world.getClass().getName());
                return VoxelShapes.empty();
            }
        }
        VoxelShape shape = mainState.getOutlineShape(world, mainPos, context);
        BlockPos offset = pos.subtract(mainPos);
        //TODO: Can we somehow cache the withOffset? It potentially would have to then be moved into the Tile, but that is probably fine
        return shape.offset(-offset.getX(), -offset.getY(), -offset.getZ());
    }

    @Override
    @Deprecated
    public boolean canPathfindThrough(@NotNull BlockState state, @NotNull BlockView world, @NotNull BlockPos pos, @NotNull NavigationType type) {
        //Mark that bounding blocks do not allow movement for use by AI pathing
        return false;
    }
}

