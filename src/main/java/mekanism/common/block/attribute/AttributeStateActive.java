package mekanism.common.block.attribute;

import mekanism.common.block.states.BlockStateHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttributeStateActive implements AttributeState {

    private static final BooleanProperty activeProperty = BooleanProperty.of("active");

    private final int ambientLight;

    AttributeStateActive(int ambientLight) {
        this.ambientLight = ambientLight;
    }

    public boolean isActive(BlockState state) {
        return state.get(activeProperty);
    }

    public BlockState setActive(@NotNull BlockState state, boolean active) {
        return state.with(activeProperty, active);
    }

    @Override
    public BlockState copyStateData(BlockState oldState, BlockState newState) {
        if (Attribute.has(newState.getBlock(), AttributeStateActive.class)) {
            newState = newState.with(activeProperty, oldState.get(activeProperty));
        }
        return newState;
    }

    @Override
    public BlockState getDefaultState(@NotNull BlockState state) {
        return state.with(activeProperty, false);
    }

    @Override
    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {
        properties.add(activeProperty);
    }

    @Override
    public void adjustProperties(AbstractBlock.Settings props) {
        if (ambientLight > 0) {
            //If we have ambient light, adjust the light level to factor in the ambient light level when it is active
            BlockStateHelper.applyLightLevelAdjustments(props, state -> isActive(state) ? ambientLight : 0);
        }
    }
}

