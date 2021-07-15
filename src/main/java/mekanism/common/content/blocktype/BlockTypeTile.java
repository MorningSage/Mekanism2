package mekanism.common.content.blocktype;

import mekanism.api.text.ILangEntry;
import mekanism.common.Mekanism;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.sound.SoundEvent;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockTypeTile<TILE extends TileEntityMekanism> extends BlockType {

    private final Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar;

    public BlockTypeTile(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
        super(description);
        this.tileEntityRegistrar = tileEntityRegistrar;
    }

    public BlockEntityType<TILE> getTileType() {
        return tileEntityRegistrar.get().getTileEntityType();
    }

    public static class BlockTileBuilder<BLOCK extends BlockTypeTile<TILE>, TILE extends TileEntityMekanism, T extends BlockTileBuilder<BLOCK, TILE, T>>
        extends BlockTypeBuilder<BLOCK, T> {

        protected BlockTileBuilder(BLOCK holder) {
            super(holder);
        }

        public static <TILE extends TileEntityMekanism> BlockTileBuilder<BlockTypeTile<TILE>, TILE, ?> createBlock(
            Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
            return new BlockTileBuilder<>(new BlockTypeTile<>(tileEntityRegistrar, description));
        }

    }
}
