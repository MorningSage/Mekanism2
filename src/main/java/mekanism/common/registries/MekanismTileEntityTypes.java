package mekanism.common.registries;

import mekanism.common.Mekanism;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.TileEntityAdvancedBoundingBlock;
import mekanism.common.tile.TileEntityBoundingBlock;
import mekanism.common.tile.TileEntityCardboardBox;
import mekanism.common.tile.machine.TileEntityDigitalMiner;

public class MekanismTileEntityTypes {

    private MekanismTileEntityTypes() {
    }

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekanism.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityBoundingBlock> BOUNDING_BLOCK = TILE_ENTITY_TYPES.register(MekanismBlocks.BOUNDING_BLOCK, TileEntityBoundingBlock::new);
    public static final TileEntityTypeRegistryObject<TileEntityBoundingBlock> ADVANCED_BOUNDING_BLOCK = TILE_ENTITY_TYPES.register(MekanismBlocks.ADVANCED_BOUNDING_BLOCK, TileEntityAdvancedBoundingBlock::new);

    //Regular Tiles
    public static final TileEntityTypeRegistryObject<TileEntityCardboardBox> CARDBOARD_BOX = TILE_ENTITY_TYPES.register(MekanismBlocks.CARDBOARD_BOX, TileEntityCardboardBox::new);
    public static final TileEntityTypeRegistryObject<TileEntityDigitalMiner> DIGITAL_MINER = TILE_ENTITY_TYPES.register(MekanismBlocks.DIGITAL_MINER, TileEntityDigitalMiner::new);

}
