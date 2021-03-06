package mekanism.mixin;

import mekanism.api.events.ItemEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {

    @Shadow @Final private PlayerEntity player;

    @Shadow @Final private CraftingInventory input;

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V",
            shift = At.Shift.AFTER
        ),
        method = "onCrafted(Lnet/minecraft/item/ItemStack;)V"
    )
    protected void onCrafted(ItemStack stack, CallbackInfo callbackInfo) {

        ItemEvents.ON_CRAFTED.invoker().onCrafted(this.player, stack, this.input);

    }

}
