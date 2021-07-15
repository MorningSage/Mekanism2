package mekanism.common.capabilities.basic;

import mekanism.api.IAlloyInteraction;
import mekanism.api.tier.AlloyTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class DefaultAlloyInteraction implements IAlloyInteraction {

    @Override
    public void onAlloyInteraction(PlayerEntity player, Hand hand, ItemStack stack, @Nonnull AlloyTier tier) {
    }

    @Override public void readFromNbt(@NotNull NbtCompound tag) { }
    @Override public void writeToNbt(@NotNull NbtCompound tag) { }
}