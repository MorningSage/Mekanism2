package mekanism.common.capabilities.basic;

import mekanism.api.IConfigCardAccess;
import mekanism.common.MekanismLang;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class DefaultConfigCardAccess implements IConfigCardAccess {
    @Override
    public String getConfigCardName() {
        return MekanismLang.NONE.getTranslationKey();
    }

    @Override
    public NbtCompound getConfigurationData(PlayerEntity player) {
        return new NbtCompound();
    }

    @Override
    public void setConfigurationData(PlayerEntity player, NbtCompound data) {
    }

    @Override
    public BlockEntityType<?> getConfigurationDataType() {
        return null;
    }

    @Override
    public void configurationDataSet() {
    }

    @Override public void readFromNbt(@NotNull NbtCompound tag) { }
    @Override public void writeToNbt(@NotNull NbtCompound tag) { }
}