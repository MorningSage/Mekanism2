package mekanism.common.block.attribute;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AttributeState extends Attribute {

    BlockState copyStateData(BlockState oldState, BlockState newState);

    void fillBlockStateContainer(Block block, List<Property<?>> properties);

    default BlockState getDefaultState(@NotNull BlockState state) {
        return state;
    }

    @Contract("_, null, _, _, _, _ -> null")
    default BlockState getStateForPlacement(Block block, @Nullable BlockState state, @NotNull WorldAccess world, @NotNull BlockPos pos, @Nullable PlayerEntity player, @NotNull Direction face) {
        return state;
    }
}
