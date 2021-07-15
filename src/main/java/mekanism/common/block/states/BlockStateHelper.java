package mekanism.common.block.states;

import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeState;
import mekanism.common.util.AccessorUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class BlockStateHelper {

    private BlockStateHelper() {
    }

    //Cardboard Box storage
    public static final BooleanProperty storageProperty = BooleanProperty.of("storage");

    public static BlockState getDefaultState(@NotNull BlockState state) {
        Block block = state.getBlock();
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                state = ((AttributeState) attr).getDefaultState(state);
            }
        }
        return state;
    }

    public static void fillBlockStateContainer(Block block, StateManager.Builder<Block, BlockState> builder) {
        List<Property<?>> properties = new ArrayList<>();
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                ((AttributeState) attr).fillBlockStateContainer(block, properties);
            }
        }
        if (block instanceof IStateStorage) {
            properties.add(storageProperty);
        }
        if (!properties.isEmpty()) {
            builder.add(properties.toArray(new Property[0]));
        }
    }

    /**
     * Helper to "hack" in and modify the light value precalculator for states to be able to use as a base level the value already set, but also modify it based on which
     * fluid a block may be fluid logged with and then use that light level instead if it is higher.
     */
    public static AbstractBlock.Settings applyLightLevelAdjustments(AbstractBlock.Settings properties) {
        return applyLightLevelAdjustments(properties, state -> 0);
    }

    /**
     * Helper to "hack" in and modify the light value precalculator for states to be able to use as a base level the value already set, but also modify it based on
     * another function to allow for compounding the light values and then using that light level instead if it is higher.
     */
    public static AbstractBlock.Settings applyLightLevelAdjustments(AbstractBlock.Settings properties, ToIntFunction<BlockState> toApply) {
        //Cache what the current light level function is
        ToIntFunction<BlockState> existingLightLevelFunction = AccessorUtils.getLuminance(properties);
        //And override the one in the properties in a way that we can modify if we have state information that should adjust it
        return properties.luminance(state -> Math.max(existingLightLevelFunction.applyAsInt(state), toApply.applyAsInt(state)));
    }

    @Contract("_, null, _ -> null")
    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, ItemPlacementContext context) {
        return getStateForPlacement(block, state, context.getWorld(), context.getBlockPos(), context.getPlayer(), context.getSide());
    }

    @Contract("_, null, _, _, _, _ -> null")
    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, @NotNull WorldAccess world, @NotNull BlockPos pos, @Nullable PlayerEntity player, @NotNull Direction face) {
        if (state == null) {
            return null;
        }
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                state = ((AttributeState) attr).getStateForPlacement(block, state, world, pos, player, face);
            }
        }
        return state;
    }

    public static BlockState copyStateData(BlockState oldState, BlockState newState) {
        Block oldBlock = oldState.getBlock();
        Block newBlock = newState.getBlock();
        for (Attribute attr : Attribute.getAll(oldBlock)) {
            if (attr instanceof AttributeState) {
                newState = ((AttributeState) attr).copyStateData(oldState, newState);
            }
        }
        if (oldBlock instanceof IStateStorage && newBlock instanceof IStateStorage) {
            newState = newState.with(storageProperty, oldState.get(storageProperty));
        }
        return newState;
    }
}

