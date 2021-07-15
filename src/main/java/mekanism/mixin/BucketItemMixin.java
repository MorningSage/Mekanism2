package mekanism.mixin;

import mekanism.common.fluid.MekanismFluid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @Shadow @Final private Fluid fluid;

    @Inject(
        at = @At("HEAD"),
        method = "playEmptyingSound",
        cancellable = true
    )
    protected void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, CallbackInfo callbackInfo) {
        if (this.fluid instanceof MekanismFluid) {
            ((MekanismFluid) fluid).getBucketEmptySound().ifPresent(soundEvent -> {
                world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(player, GameEvent.FLUID_PLACE, pos);
            });
            callbackInfo.cancel();
        }
    }

}
