package mekanism.common.tile.base;

import mekanism.api.MekanismAPI;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.block.interfaces.IHasTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

//TODO: We need to move the "supports" methods into the source interfaces so that we make sure they get checked before being used
public abstract class TileEntityMekanism extends CapabilityTileEntity {

    protected final IBlockProvider blockProvider;

    public TileEntityMekanism(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(((IHasTileEntity<? extends BlockEntity>) blockProvider.getBlock()).getTileType(), pos, state);
        this.blockProvider = blockProvider;
    }

    public Block getBlockType() {
        return blockProvider.getBlock();
    }

    public void onPlace() {
    }

    @Override
    public void markRemoved() {
        super.markRemoved();

    }

    @NotNull
    public Text getName() {
        return TextComponentUtil.translate(Util.createTranslationKey("container", Registry.BLOCK.getId(getBlockType())));
    }
}
