package mekanism.common.registries;

import mekanism.api.tier.AlloyTier;
import mekanism.common.Mekanism;
import mekanism.common.item.ItemAlloy;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.util.Rarity;

public class MekanismItems {

    private MekanismItems() {
    }

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Mekanism.MODID);

    public static final ItemRegistryObject<ItemAlloy> ATOMIC_ALLOY = registerAlloy(AlloyTier.ATOMIC, Rarity.EPIC);

    private static ItemRegistryObject<ItemAlloy> registerAlloy(AlloyTier tier, Rarity rarity) {
        return ITEMS.register("alloy_" + tier.getName(), properties -> new ItemAlloy(tier, properties.rarity(rarity)));
    }
}
