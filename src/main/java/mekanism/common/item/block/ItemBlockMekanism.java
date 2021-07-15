package mekanism.common.item.block;

import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.ITier;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class ItemBlockMekanism<BLOCK extends Block> extends BlockItem {

    @NotNull
    private final BLOCK block;

    public ItemBlockMekanism(@NotNull BLOCK block, Item.Settings properties) {
        super(block, properties);
        this.block = block;
    }

    @NotNull
    @Override
    public BLOCK getBlock() {
        return block;
    }

    public ITier getTier() {
        return null;
    }

    public EnumColor getTextColor(ItemStack stack) {
        return getTier() != null ? getTier().getBaseTier().getTextColor() : null;
    }

    @NotNull
    @Override
    public Text getName(@NotNull ItemStack stack) {
        EnumColor color = getTextColor(stack);
        if (color == null) {
            return super.getName(stack);
        }
        return TextComponentUtil.build(color, super.getName(stack));
    }
}

