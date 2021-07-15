package mekanism.common.block.prefab;

import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.UnaryOperator;

public class BlockTile<TILE extends TileEntityMekanism, TYPE extends BlockTypeTile<TILE>> extends BlockBase<TYPE> implements IHasTileEntity<TILE> {

    public BlockTile(TYPE type) {
        this(type, UnaryOperator.identity());
    }

    public BlockTile(TYPE type, UnaryOperator<AbstractBlock.Settings> propertiesModifier) {
        this(type, propertiesModifier.apply(AbstractBlock.Settings.of(Material.METAL).strength(3.5F, 16).requiresTool()));
        //TODO - 10.1: Figure out what the resistance should be (it used to be different in 1.12)
    }

    public BlockTile(TYPE type, AbstractBlock.Settings properties) {
        super(type, properties);
    }

    @Override
    public BlockEntityType<TILE> getTileType() {
        return type.getTileType();
    }

    public static class BlockTileModel<TILE extends TileEntityMekanism, BLOCK extends BlockTypeTile<TILE>> extends BlockTile<TILE, BLOCK> {

        public BlockTileModel(BLOCK type) {
            super(type);
        }
    }
}
