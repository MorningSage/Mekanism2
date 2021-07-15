package mekanism.api.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface ItemEvents {

    Event<ItemEvents> ON_CRAFTED = EventFactory
        .createArrayBacked(ItemEvents.class, callbacks ->
            (PlayerEntity player, ItemStack crafted, Inventory craftMatrix) -> {
            for (ItemEvents callback : callbacks) {
                callback.onCrafted(player, crafted, craftMatrix);
            }
        });

    void onCrafted(PlayerEntity player, ItemStack crafted, Inventory craftMatrix);
}
