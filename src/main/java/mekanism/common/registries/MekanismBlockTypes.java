package mekanism.common.registries;

import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeCustomSelectionBox;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.tile.machine.TileEntityDigitalMiner;

public final class MekanismBlockTypes {

    private MekanismBlockTypes() {
    }

    // Digital Miner
    public static final Machine<TileEntityDigitalMiner> DIGITAL_MINER = Machine.MachineBuilder
        .createMachine(() -> MekanismTileEntityTypes.DIGITAL_MINER, MekanismLang.DESCRIPTION_DIGITAL_MINER)
        //.withGui(() -> MekanismContainerTypes.DIGITAL_MINER)
        //.withEnergyConfig(MekanismConfig.usage.digitalMiner, MekanismConfig.storage.digitalMiner)
        //.withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.ANCHOR, Upgrade.STONE_GENERATOR))
        .withCustomShape(BlockShapes.DIGITAL_MINER)
        .with(AttributeCustomSelectionBox.JSON)
        //.withComputerSupport("digitalMiner")
        .replace(Attributes.ACTIVE)
        .build();
}
