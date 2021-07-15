package mekanism.common;

import mekanism.common.registries.MekanismItems;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class CreativeTabMekanism extends ItemGroup {

    private static int expandArray() {
        ((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
        return ItemGroup.GROUPS.length - 1;
    }

    public CreativeTabMekanism() {
        super(expandArray(), Mekanism.MODID);
    }

    @NotNull
    @Override
    public ItemStack createIcon() {
        return MekanismItems.ATOMIC_ALLOY.getItemStack();
    }

    @Override
    public Text getTranslationKey() {
        return MekanismLang.MEKANISM.translate();
    }
}
