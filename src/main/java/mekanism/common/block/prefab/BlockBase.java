package mekanism.common.block.prefab;

import mekanism.api.text.ILangEntry;
import mekanism.common.block.BlockMekanism;
import mekanism.common.block.attribute.AttributeCustomShape;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.interfaces.ITypeBlock;
import mekanism.common.content.blocktype.BlockType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public class BlockBase<TYPE extends BlockType> extends BlockMekanism implements IHasDescription, ITypeBlock {

    protected final TYPE type;

    public BlockBase(TYPE type, UnaryOperator<AbstractBlock.Settings> propertyModifier) {
        this(type, propertyModifier.apply(AbstractBlock.Settings.of(Material.METAL).requiresTool()));
    }

    public BlockBase(TYPE type, AbstractBlock.Settings properties) {
        super(hack(type, properties));
        this.type = type;
    }

    // ugly hack but required to have a reference to our block type before setting state info; assumes single-threaded startup
    private static BlockType cacheType;

    private static <TYPE extends BlockType> AbstractBlock.Settings hack(TYPE type, AbstractBlock.Settings props) {
        cacheType = type;
        type.getAll().forEach(a -> a.adjustProperties(props));
        return props;
    }

    @Override
    public BlockType getType() {
        return type == null ? cacheType : type;
    }

    @NotNull
    @Override
    public ILangEntry getDescription() {
        return type.getDescription();
    }

    @Override
    @Deprecated
    public boolean canPathfindThrough(@NotNull BlockState state, @NotNull BlockView world, @NotNull BlockPos pos, @NotNull NavigationType pathType) {
        //If we have a custom shape which means we are not a full block then mark that movement is not
        // allowed through this block it is not a full block. Otherwise use the normal handling for if movement is allowed
        return !type.has(AttributeCustomShape.class) && super.canPathfindThrough(state, world, pos, pathType);
    }

    @NotNull
    @Override
    @Deprecated
    public VoxelShape getOutlineShape(@NotNull BlockState state, @NotNull BlockView world, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        if (type.has(AttributeCustomShape.class)) {
            AttributeStateFacing attr = type.get(AttributeStateFacing.class);
            int index = attr == null ? 0 : (attr.getDirection(state).ordinal() - (attr.getFacingProperty() == Properties.FACING ? 0 : 2));
            return type.get(AttributeCustomShape.class).getBounds()[index];
        }
        return super.getOutlineShape(state, world, pos, context);
    }

}
