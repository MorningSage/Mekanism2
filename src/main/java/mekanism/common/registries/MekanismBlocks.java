package mekanism.common.registries;

import mekanism.common.Mekanism;
import mekanism.common.block.BlockBounding;
import mekanism.common.block.BlockCardboardBox;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockCardboardBox;
import mekanism.common.item.block.machine.ItemBlockDigitalMiner;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.tile.machine.TileEntityDigitalMiner;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class MekanismBlocks {

    private MekanismBlocks() {
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Mekanism.MODID);

    public static final BlockRegistryObject<BlockBounding, BlockItem> BOUNDING_BLOCK = registerBoundingBlock("bounding_block", () -> new BlockBounding(false));
    public static final BlockRegistryObject<BlockBounding, BlockItem> ADVANCED_BOUNDING_BLOCK = registerBoundingBlock("advanced_bounding_block", () -> new BlockBounding(true));

    public static final BlockRegistryObject<BlockTileModel<TileEntityDigitalMiner, Machine<TileEntityDigitalMiner>>, ItemBlockDigitalMiner> DIGITAL_MINER = BLOCKS.register("digital_miner", () -> new BlockTileModel<>(MekanismBlockTypes.DIGITAL_MINER), ItemBlockDigitalMiner::new);
    public static final BlockRegistryObject<BlockCardboardBox, ItemBlockCardboardBox> CARDBOARD_BOX = BLOCKS.register("cardboard_box", BlockCardboardBox::new, ItemBlockCardboardBox::new);

    private static BlockRegistryObject<BlockBounding, BlockItem> registerBoundingBlock(String name, Supplier<BlockBounding> blockSupplier) {
        return BLOCKS.register(name, blockSupplier, block -> new BlockItem(block, new Item.Settings()));
    }
}
