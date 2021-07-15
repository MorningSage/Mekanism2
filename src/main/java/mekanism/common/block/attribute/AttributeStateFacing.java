package mekanism.common.block.attribute;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class AttributeStateFacing implements AttributeState {

    private final DirectionProperty facingProperty;
    private final FacePlacementType placementType;
    private final boolean canRotate;

    public AttributeStateFacing() {
        this(true);
    }

    public AttributeStateFacing(boolean canRotate) {
        this(Properties.HORIZONTAL_FACING, canRotate);
    }

    public AttributeStateFacing(DirectionProperty facingProperty) {
        this(facingProperty, true);
    }

    public AttributeStateFacing(DirectionProperty facingProperty, boolean canRotate) {
        this(facingProperty, FacePlacementType.PLAYER_LOCATION, canRotate);
    }

    public AttributeStateFacing(DirectionProperty facingProperty, FacePlacementType placementType) {
        this(facingProperty, placementType, true);
    }

    public AttributeStateFacing(DirectionProperty facingProperty, FacePlacementType placementType, boolean canRotate) {
        this.facingProperty = facingProperty;
        this.placementType = placementType;
        this.canRotate = canRotate;
    }

    public boolean canRotate() {
        return canRotate;
    }

    public Direction getDirection(BlockState state) {
        return state.get(getFacingProperty());
    }

    public BlockState setDirection(@NotNull BlockState state, Direction newDirection) {
        return supportsDirection(newDirection) ? state.with(getFacingProperty(), newDirection) : state;
    }

    @NotNull
    public DirectionProperty getFacingProperty() {
        return facingProperty;
    }

    @NotNull
    public FacePlacementType getPlacementType() {
        return placementType;
    }

    public Collection<Direction> getSupportedDirections() {
        return getFacingProperty().getValues();
    }

    public boolean supportsDirection(Direction direction) {
        return getSupportedDirections().contains(direction);
    }

    @Override
    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {
        properties.add(getFacingProperty());
    }

    @Override
    public BlockState copyStateData(BlockState oldState, BlockState newState) {
        if (Attribute.has(newState.getBlock(), AttributeStateFacing.class)) {
            DirectionProperty oldFacingProperty = Attribute.get(oldState.getBlock(), AttributeStateFacing.class).getFacingProperty();
            newState = newState.with(Attribute.get(newState.getBlock(), AttributeStateFacing.class).getFacingProperty(), oldState.get(oldFacingProperty));
        }
        return newState;
    }

    @Override
    @Contract("_, null, _, _, _, _ -> null")
    public BlockState getStateForPlacement(Block block, @Nullable BlockState state, @NotNull WorldAccess world, @NotNull BlockPos pos, @Nullable PlayerEntity player, @NotNull Direction face) {
        if (state == null) return null;
        AttributeStateFacing blockFacing = Attribute.get(block, AttributeStateFacing.class);
        Direction newDirection = Direction.SOUTH;
        if (blockFacing.getPlacementType() == FacePlacementType.PLAYER_LOCATION) {
            //TODO: Somehow weight this stuff towards context.getFace(), so that it has a higher likelihood of going with the face that was clicked on
            if (blockFacing.supportsDirection(Direction.DOWN) && blockFacing.supportsDirection(Direction.UP)) {
                float rotationPitch = player == null ? 0 : player.getPitch();
                int height = Math.round(rotationPitch);
                if (height >= 65) {
                    newDirection = Direction.UP;
                } else if (height <= -65) {
                    newDirection = Direction.DOWN;
                }
            }
            if (newDirection != Direction.DOWN && newDirection != Direction.UP) {
                //TODO: Can this just use newDirection = context.getPlacementHorizontalFacing().getOpposite(); or is that not accurate
                float placementYaw = player == null ? 0 : player.getYaw();
                int side = MathHelper.floor((placementYaw * 4.0F / 360.0F) + 0.5D) & 3;
                newDirection = switch (side) {
                    case 0  -> Direction.NORTH;
                    case 1  -> Direction.EAST;
                    case 2  -> Direction.SOUTH;
                    case 3  -> Direction.WEST;
                    default -> newDirection;
                };
            }

        } else {
            newDirection = blockFacing.supportsDirection(face) ? face : Direction.NORTH;
        }

        state = blockFacing.setDirection(state, newDirection);
        return state;
    }

    public static BlockState rotate(BlockState state, WorldAccess world, BlockPos pos, BlockRotation rotation) {
        return rotate(state, rotation);
    }

    public static BlockState rotate(BlockState state, BlockRotation rotation) {
        Block block = state.getBlock();
        if (Attribute.has(block, AttributeStateFacing.class)) {
            AttributeStateFacing blockFacing = Attribute.get(block, AttributeStateFacing.class);
            if (blockFacing.canRotate()) {
                return rotate(blockFacing, blockFacing.getFacingProperty(), state, rotation);
            }
        }
        return state;
    }

    public static BlockState mirror(BlockState state, BlockMirror mirror) {
        Block block = state.getBlock();
        if (Attribute.has(block, AttributeStateFacing.class)) {
            AttributeStateFacing blockFacing = Attribute.get(block, AttributeStateFacing.class);
            if (blockFacing.canRotate()) {
                DirectionProperty property = blockFacing.getFacingProperty();
                return rotate(blockFacing, property, state, mirror.getRotation(state.get(property)));
            }
        }
        return state;
    }

    private static BlockState rotate(AttributeStateFacing blockFacing, DirectionProperty property, BlockState state, BlockRotation rotation) {
        return blockFacing.setDirection(state, rotation.rotate(state.get(property)));
    }

    public enum FacePlacementType {
        /** Set the face based on the player's relative location to the placement location. */
        PLAYER_LOCATION,
        /** Set the face based on the direction of the block face selected. */
        SELECTED_FACE
    }
}
