package mekanism.common.content.blocktype;

import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeCustomShape;
import mekanism.common.block.interfaces.ITypeBlock;
import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlockType {

    private final ILangEntry description;
    private final Map<Class<? extends Attribute>, Attribute> attributeMap = new HashMap<>();

    public BlockType(ILangEntry description) {
        this.description = description;
    }

    public boolean has(Class<? extends Attribute> type) {
        return attributeMap.containsKey(type);
    }

    public Collection<Attribute> getAll() {
        return attributeMap.values();
    }

    @NotNull
    public ILangEntry getDescription() {
        return description;
    }

    public static boolean is(Block block, BlockType... types) {
        if (block instanceof ITypeBlock) {
            for (BlockType type : types) {
                if (((ITypeBlock) block).getType() == type) {
                    return true;
                }
            }
        }
        return false;
    }

    public static BlockType get(Block block) {
        return block instanceof ITypeBlock ? ((ITypeBlock) block).getType() : null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Attribute> T get(Class<T> type) {
        return (T) attributeMap.get(type);
    }


    public void add(Attribute... attrs) {
        for (Attribute attr : attrs) {
            attributeMap.put(attr.getClass(), attr);
        }
    }

    public static class BlockTypeBuilder<BLOCK extends BlockType, T extends BlockTypeBuilder<BLOCK, T>> {

        protected final BLOCK holder;

        protected BlockTypeBuilder(BLOCK holder) {
            this.holder = holder;
        }

        public static BlockTypeBuilder<BlockType, ?> createBlock(ILangEntry description) {
            return new BlockTypeBuilder<>(new BlockType(description));
        }

        /**
         * This is the same as {@link #with(Attribute...)} except exists to make it more clear that we are replacing/overriding an existing attribute we added.
         */
        public final T replace(Attribute... attrs) {
            return with(attrs);
        }

        public final T with(Attribute... attrs) {
            holder.add(attrs);
            return getThis();
        }
        @SuppressWarnings("unchecked")
        public T getThis() {
            return (T) this;
        }
        public T withCustomShape(VoxelShape[] shape) {
            return with(new AttributeCustomShape(shape));
        }
        public BLOCK build() {
            return holder;
        }
    }
}


