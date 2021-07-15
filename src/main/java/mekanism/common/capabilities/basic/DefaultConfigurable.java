package mekanism.common.capabilities.basic;

import mekanism.api.IConfigurable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ben on 19/05/16.
 */
public class DefaultConfigurable implements IConfigurable {

    @Override
    public ActionResult onSneakRightClick(PlayerEntity player, Direction side) {
        return ActionResult.PASS;
    }

    @Override
    public ActionResult onRightClick(PlayerEntity player, Direction side) {
        return ActionResult.PASS;
    }


    @Override public void readFromNbt(@NotNull NbtCompound tag) { }
    @Override public void writeToNbt(@NotNull NbtCompound tag) { }
}