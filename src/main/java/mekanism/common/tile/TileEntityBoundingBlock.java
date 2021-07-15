package mekanism.common.tile;

import mekanism.common.Mekanism;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tile.base.TileEntityUpdateable;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Multi-block used by wind turbines, solar panels, and other machines
 */
public class TileEntityBoundingBlock extends TileEntityUpdateable {

    private BlockPos mainPos = BlockPos.ORIGIN;

    public boolean receivedCoords;
    private int currentRedstoneLevel;

    public TileEntityBoundingBlock(BlockPos pos, BlockState state) {
        this(MekanismTileEntityTypes.BOUNDING_BLOCK.getTileEntityType(), pos, state);
    }

    public TileEntityBoundingBlock(BlockEntityType<TileEntityBoundingBlock> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setMainLocation(BlockPos pos) {
        receivedCoords = pos != null;
        if (!world.isClient()) {
            mainPos = pos;
            //sendUpdatePacket();
        }
    }

    public BlockPos getMainPos() {
        if (mainPos == null) {
            mainPos = BlockPos.ORIGIN;
        }
        return mainPos;
    }

}

