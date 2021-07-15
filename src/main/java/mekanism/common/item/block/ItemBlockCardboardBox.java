package mekanism.common.item.block;

import mekanism.api.text.EnumColor;
import mekanism.common.MekanismLang;
import mekanism.common.block.BlockCardboardBox;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.tile.TileEntityCardboardBox;
import mekanism.common.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemBlockCardboardBox extends ItemBlockMekanism<BlockCardboardBox> {

    public ItemBlockCardboardBox(BlockCardboardBox block) {
        super(block, ItemDeferredRegister.getMekBaseProperties().maxCount(16));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(@NotNull ItemStack stack, World world, @NotNull List<Text> tooltip, @NotNull TooltipContext flag) {
        tooltip.add(MekanismLang.BLOCK_DATA.translateColored(EnumColor.INDIGO, "Yes"));
    }

    @Override
    public boolean place(@NotNull ItemPlacementContext context, @NotNull BlockState state) {
        World world = context.getWorld();
        if (world.isClient) {
            return true;
        }
        if (super.place(context, state)) {
            //TileEntityCardboardBox tile = WorldUtils.getTileEntity(TileEntityCardboardBox.class, world, context.getBlockPos());
            //if (tile != null) {
            //    tile.storedData = getBlockData(context.getStack());
            //}
            return true;
        }
        return false;
    }

}
