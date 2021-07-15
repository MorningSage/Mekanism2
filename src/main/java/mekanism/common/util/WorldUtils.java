package mekanism.common.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.Mekanism;
import mekanism.common.block.BlockBounding;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.TileEntityAdvancedBoundingBlock;
import mekanism.common.tile.TileEntityBoundingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class WorldUtils {

    public static boolean isInBuildLimit(@NotNull BlockView world, @NotNull BlockPos pos) {
        return !world.isOutOfHeightLimit(pos) && AccessorUtils.isValidHorizontally(pos);
    }

    /**
     * Checks if a position is in bounds of the world, and is loaded
     *
     * @param world world
     * @param pos   position
     *
     * @return True if the position is loaded or the given world is of a superclass of IWorldReader that does not have a concept of being loaded.
     */
    @Contract("null, _ -> false")
    public static boolean isBlockLoaded(@Nullable BlockView world, @NotNull BlockPos pos) {
        if (world == null || !isInBuildLimit(world, pos)) {
            return false;
        } else if (world instanceof WorldView) {
            //Note: We don't bother checking if it is a world and then isBlockPresent because
            // all that does is also validate the y value is in bounds, and we already check to make
            // sure the position is valid both in the y and xz directions
            return ((WorldView) world).isPosLoaded(pos.getX(), pos.getZ());
        }
        return true;
    }

    /**
     * Gets the chunk in a given position or {@code null} if there is no world, the position is out of bounds or the chunk isn't loaded. Tries to retrieve it from our
     * cache and if it isn't found, tries to get it from the world and adds it to our cache.
     *
     * @param world    world
     * @param chunkMap cached chunk map
     * @param pos      position
     *
     * @return The chunk in a given position or {@code null} if there is no world, the position is out of bounds or the chunk isn't loaded
     */
    @Nullable
    @Contract("null, _, _ -> null")
    private static Chunk getChunkForPos(@Nullable WorldAccess world, @NotNull Long2ObjectMap<Chunk> chunkMap, @NotNull BlockPos pos) {
        if (world == null || !isInBuildLimit(world, pos)) {
            //Allow the world to be nullable to remove warnings when we are calling things from a place that world could be null
            // Also short circuit to check if the position is out of bounds before bothering to lookup the chunk
            return null;
        }
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        long combinedChunk = (((long) chunkX) << 32) | (chunkZ & 0xFFFFFFFFL);
        //We get the chunk rather than the world so we can cache the chunk improving the overall
        // performance for retrieving a bunch of chunks in the general vicinity
        Chunk chunk = chunkMap.get(combinedChunk);
        if (chunk == null) {
            //Get the chunk but don't force load it
            chunk = world.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
            if (chunk != null) {
                chunkMap.put(combinedChunk, chunk);
            }
        }
        return chunk;
    }

    /**
     * Gets a blockstate if the location is loaded by getting the chunk from the passed in cache of chunks rather than directly using the world. We then store our chunk
     * we found back in the cache so as to more quickly be able to lookup chunks if we are doing lots of lookups at once (For example multiblock structure validation)
     *
     * @param world    world
     * @param chunkMap cached chunk map
     * @param pos      position
     *
     * @return optional containing the blockstate if found, empty optional if not loaded
     */
    @NotNull
    public static Optional<BlockState> getBlockState(@Nullable WorldAccess world, @NotNull Long2ObjectMap<Chunk> chunkMap, @NotNull BlockPos pos) {
        //Get the blockstate using the chunk we found/had cached
        return getBlockState(getChunkForPos(world, chunkMap, pos), pos);
    }

    /**
     * Gets a blockstate if the location is loaded
     *
     * @param world world
     * @param pos   position
     *
     * @return optional containing the blockstate if found, empty optional if not loaded
     */
    @NotNull
    public static Optional<BlockState> getBlockState(@Nullable BlockView world, @NotNull BlockPos pos) {
        if (!isBlockLoaded(world, pos)) {
            //If the world is null or its a world reader and the block is not loaded, return empty
            return Optional.empty();
        }
        return Optional.of(world.getBlockState(pos));
    }

    /**
     * Gets a fluidstate if the location is loaded by getting the chunk from the passed in cache of chunks rather than directly using the world. We then store our chunk
     * we found back in the cache so as to more quickly be able to lookup chunks if we are doing lots of lookups at once (For example multiblock structure validation)
     *
     * @param world    world
     * @param chunkMap cached chunk map
     * @param pos      position
     *
     * @return optional containing the fluidstate if found, empty optional if not loaded
     */
    @NotNull
    public static Optional<FluidState> getFluidState(@Nullable WorldAccess world, @NotNull Long2ObjectMap<Chunk> chunkMap, @NotNull BlockPos pos) {
        //Get the fluidstate using the chunk we found/had cached
        return getFluidState(getChunkForPos(world, chunkMap, pos), pos);
    }

    /**
     * Gets a fluidstate if the location is loaded
     *
     * @param world world
     * @param pos   position
     *
     * @return optional containing the fluidstate if found, empty optional if not loaded
     */
    @NotNull
    public static Optional<FluidState> getFluidState(@Nullable BlockView world, @NotNull BlockPos pos) {
        if (!isBlockLoaded(world, pos)) {
            //If the world is null or its a world reader and the block is not loaded, return empty
            return Optional.empty();
        }
        return Optional.of(world.getFluidState(pos));
    }

    /**
     * Gets a tile entity if the location is loaded by getting the chunk from the passed in cache of chunks rather than directly using the world. We then store our chunk
     * we found back in the cache so as to more quickly be able to lookup chunks if we are doing lots of lookups at once (For example the transporter pathfinding)
     *
     * @param world    world
     * @param chunkMap cached chunk map
     * @param pos      position
     *
     * @return tile entity if found, null if either not found or not loaded
     */
    @Nullable
    @Contract("null, _, _ -> null")
    public static BlockEntity getTileEntity(@Nullable WorldAccess world, @NotNull Long2ObjectMap<Chunk> chunkMap, @NotNull BlockPos pos) {
        //Get the tile entity using the chunk we found/had cached
        return getTileEntity(getChunkForPos(world, chunkMap, pos), pos);
    }

    /**
     * Gets a tile entity if the location is loaded by getting the chunk from the passed in cache of chunks rather than directly using the world. We then store our chunk
     * we found back in the cache so as to more quickly be able to lookup chunks if we are doing lots of lookups at once (For example the transporter pathfinding)
     *
     * @param clazz    Class type of the TileEntity we expect to be in the position
     * @param world    world
     * @param chunkMap cached chunk map
     * @param pos      position
     *
     * @return tile entity if found, null if either not found, not loaded, or of the wrong type
     */
    @Nullable
    @Contract("_, null, _, _ -> null")
    public static <T extends BlockEntity> T getTileEntity(@NotNull Class<T> clazz, @Nullable WorldAccess world, @NotNull Long2ObjectMap<Chunk> chunkMap, @NotNull BlockPos pos) {
        return getTileEntity(clazz, world, chunkMap, pos, false);
    }

    /**
     * Gets a tile entity if the location is loaded by getting the chunk from the passed in cache of chunks rather than directly using the world. We then store our chunk
     * we found back in the cache so as to more quickly be able to lookup chunks if we are doing lots of lookups at once (For example the transporter pathfinding)
     *
     * @param clazz        Class type of the TileEntity we expect to be in the position
     * @param world        world
     * @param chunkMap     cached chunk map
     * @param pos          position
     * @param logWrongType Whether or not an error should be logged if a tile of a different type is found at the position
     *
     * @return tile entity if found, null if either not found, not loaded, or of the wrong type
     */
    @Nullable
    @Contract("_, null, _, _, _ -> null")
    public static <T extends BlockEntity> T getTileEntity(@NotNull Class<T> clazz, @Nullable WorldAccess world, @NotNull Long2ObjectMap<Chunk> chunkMap, @NotNull BlockPos pos,
                                                          boolean logWrongType) {
        //Get the tile entity using the chunk we found/had cached
        return getTileEntity(clazz, getChunkForPos(world, chunkMap, pos), pos, logWrongType);
    }

    /**
     * Gets a tile entity if the location is loaded
     *
     * @param world world
     * @param pos   position
     *
     * @return tile entity if found, null if either not found or not loaded
     */
    @Nullable
    @Contract("null, _ -> null")
    public static BlockEntity getTileEntity(@Nullable BlockView world, @NotNull BlockPos pos) {
        if (!isBlockLoaded(world, pos)) {
            //If the world is null or its a world reader and the block is not loaded, return null
            return null;
        }
        return world.getBlockEntity(pos);
    }

    /**
     * Gets a tile entity if the location is loaded
     *
     * @param clazz Class type of the TileEntity we expect to be in the position
     * @param world world
     * @param pos   position
     *
     * @return tile entity if found, null if either not found, not loaded, or of the wrong type
     */
    @Nullable
    @Contract("_, null, _ -> null")
    public static <T extends BlockEntity> T getTileEntity(@NotNull Class<T> clazz, @Nullable BlockView world, @NotNull BlockPos pos) {
        return getTileEntity(clazz, world, pos, false);
    }

    /**
     * Gets a tile entity if the location is loaded
     *
     * @param clazz        Class type of the TileEntity we expect to be in the position
     * @param world        world
     * @param pos          position
     * @param logWrongType Whether or not an error should be logged if a tile of a different type is found at the position
     *
     * @return tile entity if found, null if either not found or not loaded, or of the wrong type
     */
    @Nullable
    @Contract("_, null, _, _ -> null")
    public static <T extends BlockEntity> T getTileEntity(@NotNull Class<T> clazz, @Nullable BlockView world, @NotNull BlockPos pos, boolean logWrongType) {
        BlockEntity tile = getTileEntity(world, pos);
        if (tile == null) {
            return null;
        }
        if (clazz.isInstance(tile)) {
            return clazz.cast(tile);
        } else if (logWrongType) {
            Mekanism.logger.warn("Unexpected TileEntity class at {}, expected {}, but found: {}", pos, clazz, tile.getClass());
        }
        return null;
    }

    /**
     * Marks the chunk this TileEntity is in as modified. Call this method to be sure NBT is written by the defined tile entity.
     *
     * @param tile TileEntity to save
     */
    public static void saveChunk(BlockEntity tile) {
        if (tile != null && !tile.isRemoved() && tile.getWorld() != null) {
            markChunkDirty(tile.getWorld(), tile.getPos());
        }
    }

    /**
     * Marks a chunk as dirty if it is currently loaded
     */
    public static void markChunkDirty(World world, BlockPos pos) {
        if (isBlockLoaded(world, pos)) {
            world.getChunk(pos).setShouldSave(true);
        }
        //TODO: This line below is now (1.16+) called by the mark chunk dirty method (without even validating if it is loaded).
        // And with it causes issues where chunks are easily ghost loaded. Why was it added like that and do we need to somehow
        // also update neighboring comparators
        //world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock()); //Notify neighbors of changes
    }

    /**
     * Dismantles a block, dropping it and removing it from the world.
     */
    public static void dismantleBlock(BlockState state, World world, BlockPos pos) {
        dismantleBlock(state, world, pos, getTileEntity(world, pos));
    }

    /**
     * Dismantles a block, dropping it and removing it from the world.
     */
    public static void dismantleBlock(BlockState state, World world, BlockPos pos, @Nullable BlockEntity tile) {
        Block.dropStacks(state, world, pos, tile);
        world.removeBlock(pos, false);
    }

    /**
     * Gets the distance to a defined Coord4D.
     *
     * @return the distance to the defined Coord4D
     */
    public static double distanceBetween(BlockPos start, BlockPos end) {
        return MathHelper.sqrt((float)start.getSquaredDistance(end));
    }

    /**
     * A method used to find the Direction represented by the distance of the defined Coord4D. Most likely won't have many applicable uses.
     *
     * @return Direction representing the side the defined relative Coord4D is on to this
     */
    public static Direction sideDifference(BlockPos pos, BlockPos other) {
        BlockPos diff = pos.subtract(other);
        for (Direction side : EnumUtils.DIRECTIONS) {
            if (side.getOffsetX() == diff.getX() && side.getOffsetY() == diff.getY() && side.getOffsetZ() == diff.getZ()) {
                return side;
            }
        }
        return null;
    }

    ///**
    // * Whether or not the provided chunk is being vibrated by a Seismic Vibrator.
    // *
    // * @param chunk chunk to check
    // *
    // * @return if the chunk is being vibrated
    // */
    //public static boolean isChunkVibrated(ChunkPos chunk, World world) {
    //    return Mekanism.activeVibrators.stream().anyMatch(coord -> coord.dimension == world.dimension() && coord.getX() >> 4 == chunk.x && coord.getZ() >> 4 == chunk.z);
    //}

    //public static boolean tryPlaceContainedLiquid(@Nullable PlayerEntity player, World world, BlockPos pos, @NotNull FluidStack fluidStack, @Nullable Direction side) {
    //    Fluid fluid = fluidStack.getFluid();
    //    if (!fluid.getAttributes().canBePlacedInWorld(world, pos, fluidStack)) {
    //        //If there is no fluid or it cannot be placed in the world just
    //        return false;
    //    }
    //    BlockState state = world.getBlockState(pos);
    //    boolean isReplaceable = state.canBeReplaced(fluid);
    //    boolean canContainFluid = state.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) state.getBlock()).canPlaceLiquid(world, pos, state, fluid);
    //    if (state.isAir() || isReplaceable || canContainFluid) {
    //        if (world.dimensionType().ultraWarm() && fluid.getAttributes().doesVaporize(world, pos, fluidStack)) {
    //            fluid.getAttributes().vaporize(player, world, pos, fluidStack);
    //        } else if (canContainFluid) {
    //            if (!((ILiquidContainer) state.getBlock()).placeLiquid(world, pos, state, fluid.getAttributes().getStateForPlacement(world, pos, fluidStack))) {
    //                //If something went wrong return that we couldn't actually place it
    //                return false;
    //            }
    //            playEmptySound(player, world, pos, fluidStack);
    //        } else {
    //            if (!world.isClient() && isReplaceable && !state.getMaterial().isLiquid()) {
    //                world.destroyBlock(pos, true);
    //            }
    //            playEmptySound(player, world, pos, fluidStack);
    //            world.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), BlockFlags.DEFAULT_AND_RERENDER);
    //        }
    //        return true;
    //    }
    //    return side != null && tryPlaceContainedLiquid(player, world, pos.relative(side), fluidStack, null);
    //}

    //private static void playEmptySound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, @NotNull FluidStack fluidStack) {
    //    SoundEvent soundevent = fluidStack.getFluid().getAttributes().getEmptySound(world, pos);
    //    if (soundevent == null) {
    //        soundevent = fluidStack.getFluid().is(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
    //    }
    //    world.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    //}

    //public static void playFillSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, @NotNull FluidStack fluidStack) {
    //    SoundEvent soundevent = fluidStack.getFluid().getAttributes().getFillSound(world, pos);
    //    if (soundevent == null) {
    //        soundevent = fluidStack.getFluid().is(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL;
    //    }
    //    world.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    //}

    /**
     * Better version of the World.getRedstonePowerFromNeighbors() method that doesn't load chunks.
     *
     * @param world the world to perform the check in
     * @param pos   the position of the block performing the check
     *
     * @return if the block is indirectly getting powered by LOADED chunks
     */
    public static boolean isGettingPowered(World world, BlockPos pos) {
        for (Direction side : EnumUtils.DIRECTIONS) {
            BlockPos offset = pos.offset(side);
            if (isBlockLoaded(world, pos) && isBlockLoaded(world, offset)) {
                BlockState blockState = world.getBlockState(offset);
                boolean weakPower = blockState.isSolidBlock(world, pos);
                if (weakPower && isDirectlyGettingPowered(world, offset) || !weakPower && blockState.getWeakRedstonePower(world, offset, side) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a block is directly getting powered by any of its neighbors without loading any chunks.
     *
     * @param world the world to perform the check in
     * @param pos   the BlockPos of the block to check
     *
     * @return if the block is directly getting powered
     */
    public static boolean isDirectlyGettingPowered(World world, BlockPos pos) {
        for (Direction side : EnumUtils.DIRECTIONS) {
            BlockPos offset = pos.offset(side);
            if (isBlockLoaded(world, offset)) {
                if (world.getEmittedRedstonePower(pos, side) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if all the positions are valid and the current block in them can be replaced.
     *
     * @return True if the blocks can be replaced and is within the world's bounds.
     */
    public static boolean areBlocksValidAndReplaceable(@NotNull BlockView world, @NotNull BlockPos... positions) {
        return areBlocksValidAndReplaceable(world, Arrays.stream(positions));
    }

    /**
     * Checks if all the positions are valid and the current block in them can be replaced.
     *
     * @return True if the blocks can be replaced and is within the world's bounds.
     */
    public static boolean areBlocksValidAndReplaceable(@NotNull BlockView world, @NotNull Collection<BlockPos> positions) {
        //TODO: Potentially move more block placement over to these methods
        return areBlocksValidAndReplaceable(world, positions.stream());
    }

    /**
     * Checks if all the positions are valid and the current block in them can be replaced.
     *
     * @return True if the blocks can be replaced and is within the world's bounds.
     */
    public static boolean areBlocksValidAndReplaceable(@NotNull BlockView world, @NotNull Stream<BlockPos> positions) {
        return positions.allMatch(pos -> isValidReplaceableBlock(world, pos));
    }

    /**
     * Checks if a block is valid for a position and the current block there can be replaced.
     *
     * @return True if the block can be replaced and is within the world's bounds.
     */
    public static boolean isValidReplaceableBlock(@NotNull BlockView world, @NotNull BlockPos pos) {
        return isInBuildLimit(world, pos) && world.getBlockState(pos).getMaterial().isReplaceable();
    }

    /**
     * Notifies neighboring blocks of a TileEntity change without loading chunks.
     *
     * @param world world to perform the operation in
     * @param pos   BlockPos to perform the operation on
     */
    public static void notifyLoadedNeighborsOfTileChange(World world, BlockPos pos) {
        for (Direction dir : EnumUtils.DIRECTIONS) {
            BlockPos offset = pos.offset(dir);

            if (isBlockLoaded(world, offset)) {
                notifyNeighborOfChange(world, offset, pos);
            }
        }
    }

    /**
     * Calls BOTH neighbour changed functions because nobody can decide on which one to implement, assuming that the neighboring position is loaded.
     *
     * @param world   world the change exists in
     * @param pos     neighbor to notify
     * @param fromPos pos of our block that updated
     */
    public static void notifyNeighborOfChange(@Nullable World world, BlockPos pos, BlockPos fromPos) {
        getBlockState(world, pos).ifPresent(state -> {
            //state.onNeighborChange(world, pos, fromPos);
            state.neighborUpdate(world, pos, world.getBlockState(fromPos).getBlock(), fromPos, false);
        });
    }

    /**
     * Calls BOTH neighbour changed functions because nobody can decide on which one to implement, assuming that the neighboring position is loaded.
     *
     * @param world        world the change exists in
     * @param neighborSide The side the neighbor to notify is on
     * @param fromPos      pos of our block that updated
     */
    public static void notifyNeighborOfChange(@Nullable World world, Direction neighborSide, BlockPos fromPos) {
        notifyNeighborOfChange(world, fromPos.offset(neighborSide), fromPos);
    }

    /**
     * Places a fake bounding block at the defined location.
     *
     * @param world            world to place block in
     * @param boundingLocation coordinates of bounding block
     * @param orig             original block position
     */
    public static void makeBoundingBlock(@Nullable WorldAccess world, BlockPos boundingLocation, BlockPos orig) {
        if (world == null) return;
        BlockBounding boundingBlock = MekanismBlocks.BOUNDING_BLOCK.getBlock();
        BlockState newState = BlockStateHelper.getStateForPlacement(boundingBlock, boundingBlock.getDefaultState(), world, boundingLocation, null, Direction.NORTH);
        world.setBlockState(boundingLocation, newState, Block.NOTIFY_ALL);
        if (!world.isClient()) {
            TileEntityBoundingBlock tile = getTileEntity(TileEntityBoundingBlock.class, world, boundingLocation);
            if (tile != null) {
                tile.setMainLocation(orig);
            } else {
                Mekanism.logger.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
            }
        }
    }

    /**
     * Places a fake advanced bounding block at the defined location.
     *
     * @param world            world to place block in
     * @param boundingLocation coordinates of bounding block
     * @param orig             original block position
     */
    public static void makeAdvancedBoundingBlock(WorldAccess world, BlockPos boundingLocation, BlockPos orig) {
        BlockBounding boundingBlock = MekanismBlocks.ADVANCED_BOUNDING_BLOCK.getBlock();
        BlockState newState = BlockStateHelper.getStateForPlacement(boundingBlock, boundingBlock.getDefaultState(), world, boundingLocation, null, Direction.NORTH);
        world.setBlockState(boundingLocation, newState, Block.NOTIFY_ALL);
        if (!world.isClient()) {
            TileEntityAdvancedBoundingBlock tile = getTileEntity(TileEntityAdvancedBoundingBlock.class, world, boundingLocation);
            if (tile != null) {
                tile.setMainLocation(orig);
            } else {
                Mekanism.logger.warn("Unable to find Advanced Bounding Block Tile at: {}", boundingLocation);
            }
        }
    }

    /**
     * Marks a block for a render update if loaded.
     *
     * @param world world the block is in
     * @param pos   Position of the block
     * @param state The block state at the position
     */
    public static void updateBlock(@Nullable World world, @NotNull BlockPos pos, BlockState state) {
        if (isBlockLoaded(world, pos)) {
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        }
    }

    /**
     * Rechecks the lighting at a specific block's position if the block is loaded.
     *
     * @param world world the block is in
     * @param pos   coordinates
     */
    public static void recheckLighting(@Nullable BlockRenderView world, @NotNull BlockPos pos) {
        if (isBlockLoaded(world, pos)) {
            world.getLightingProvider().checkBlock(pos);
        }
    }

    /**
     * Vanilla copy of {@link net.minecraft.client.world.ClientWorld#method_23783(float)} used to be World#getSunBrightness
     */
    public static float getSunBrightness(World world, float partialTicks) {
        float f = world.getSkyAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float) (f1 * (1.0D - world.getRainGradient(partialTicks) * 5.0F / 16.0D));
        f1 = (float) (f1 * (1.0D - world.getThunderGradient(partialTicks) * 5.0F / 16.0D));
        return f1 * 0.8F + 0.2F;
    }

    /**
     * Checks to see if the block at the position can see the sky and it is daytime.
     *
     * @param world World to check in.
     * @param pos   Position to check.
     *
     * @return {@code true} if it can.
     */
    @Contract("null, _ -> false")
    public static boolean canSeeSun(@Nullable World world, BlockPos pos) {
        //Note: We manually handle the world#isDaytime check by just checking the subtracted skylight
        // as vanilla returns false if the world's time is set to a fixed value even if that time
        // would effectively be daytime
        return world != null && world.getDimension().hasSkyLight() && world.getAmbientDarkness() < 4 && world.isSkyVisibleAllowingSea(pos);
    }

    /**
     * Converts a {@link BlockPos} to a long representing the {@link ChunkPos} it is in without creating a temporary {@link ChunkPos} object.
     *
     * @param pos Pos to convert.
     */
    public static long getChunkPosAsLong(BlockPos pos) {
        long x = pos.getX() >> 4;
        long z = pos.getZ() >> 4;
        return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32;
    }

    /**
     * Converts a long representing a {@link ChunkPos} to a {@link BlockPos} without creating a temporary {@link ChunkPos} object.
     *
     * @param chunkPos Pos to convert.
     */
    public static BlockPos getBlockPosFromChunkPos(long chunkPos) {
        return new BlockPos((int) chunkPos, 0, (int) (chunkPos >> 32));
    }
}
