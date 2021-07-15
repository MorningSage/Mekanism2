package mekanism.common.block;

import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.block.states.IStateStorage;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tile.TileEntityCardboardBox;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;

public class BlockCardboardBox extends BlockMekanism implements IStateStorage, IHasTileEntity<TileEntityCardboardBox> {

    public BlockCardboardBox() {
        super(AbstractBlock.Settings.of(Material.WOOL).strength(0.5F, 0.6F));
    }

    @Override
    public BlockEntityType<TileEntityCardboardBox> getTileType() {
        return MekanismTileEntityTypes.CARDBOARD_BOX.getTileEntityType();
    }

}
