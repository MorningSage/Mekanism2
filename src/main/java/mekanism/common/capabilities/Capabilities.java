package mekanism.common.capabilities;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import dev.onyxstudios.cca.api.v3.util.GenericComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.util.GenericComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import mekanism.api.IAlloyInteraction;
import mekanism.api.IConfigCardAccess;
import mekanism.api.IConfigurable;
import mekanism.api.IEvaporationSolar;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.heat.IHeatHandler;
import mekanism.api.lasers.ILaserDissipation;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.radiation.capability.IRadiationEntity;
import mekanism.api.radiation.capability.IRadiationShielding;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.basic.*;
import mekanism.common.lib.radiation.capability.DefaultRadiationEntity;
import mekanism.common.lib.radiation.capability.DefaultRadiationShielding;
import mekanism.common.tile.base.CapabilityTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Capabilities implements BlockComponentInitializer, ChunkComponentInitializer, EntityComponentInitializer,
    GenericComponentInitializer, ItemComponentInitializer, LevelComponentInitializer, ScoreboardComponentInitializer,
    WorldComponentInitializer {

    public static ComponentKey<IGasHandler> GAS_HANDLER_CAPABILITY = register(Mekanism.id("gas_handler"), IGasHandler.class);
    public static ComponentKey<IInfusionHandler> INFUSION_HANDLER_CAPABILITY = register(Mekanism.id("infusion_handler"), IInfusionHandler.class);
    public static ComponentKey<IPigmentHandler> PIGMENT_HANDLER_CAPABILITY = register(Mekanism.id("pigment_handler"), IPigmentHandler.class);
    public static ComponentKey<ISlurryHandler> SLURRY_HANDLER_CAPABILITY = register(Mekanism.id("slurry_handler"), ISlurryHandler.class);
    public static ComponentKey<IHeatHandler> HEAT_HANDLER_CAPABILITY = register(Mekanism.id("heat_handler"), IHeatHandler.class);
    public static ComponentKey<IStrictEnergyHandler> STRICT_ENERGY_CAPABILITY = register(Mekanism.id("strict_energy_handler"), IStrictEnergyHandler.class);
    public static ComponentKey<IConfigurable> CONFIGURABLE_CAPABILITY = register(Mekanism.id("configurable"), IConfigurable.class);
    public static ComponentKey<IAlloyInteraction> ALLOY_INTERACTION_CAPABILITY = register(Mekanism.id("alloy_interaction"), IAlloyInteraction.class);
    public static ComponentKey<IConfigCardAccess> CONFIG_CARD_CAPABILITY = register(Mekanism.id("config_card_access"), IConfigCardAccess.class);
    public static ComponentKey<IEvaporationSolar> EVAPORATION_SOLAR_CAPABILITY = register(Mekanism.id("evaporation_solar"), IEvaporationSolar.class);
    public static ComponentKey<ILaserReceptor> LASER_RECEPTOR_CAPABILITY = register(Mekanism.id("laser_receptor"), ILaserReceptor.class);
    public static ComponentKey<ILaserDissipation> LASER_DISSIPATION_CAPABILITY = register(Mekanism.id("laser_dissipation"), ILaserDissipation.class);
    public static ComponentKey<IRadiationShielding> RADIATION_SHIELDING_CAPABILITY = register(Mekanism.id("radiation_shielding"), IRadiationShielding.class);
    public static ComponentKey<IRadiationEntity> RADIATION_ENTITY_CAPABILITY = register(Mekanism.id("radiation_entity"), IRadiationEntity.class);

    @Override
    public void registerBlockComponentFactories(@NotNull BlockComponentFactoryRegistry registry) {
        registry.registerFor(CapabilityTileEntity.class, GAS_HANDLER_CAPABILITY, (capabilityTileEntity) ->
            new DefaultChemicalHandler.DefaultGasHandler()
        );
        registry.registerFor(CapabilityTileEntity.class, INFUSION_HANDLER_CAPABILITY, (capabilityTileEntity) ->
            new DefaultChemicalHandler.DefaultInfusionHandler()
        );
        registry.registerFor(CapabilityTileEntity.class, PIGMENT_HANDLER_CAPABILITY, (capabilityTileEntity) ->
            new DefaultChemicalHandler.DefaultPigmentHandler()
        );
        registry.registerFor(CapabilityTileEntity.class, SLURRY_HANDLER_CAPABILITY, (capabilityTileEntity) ->
            new DefaultChemicalHandler.DefaultSlurryHandler()
        );
        registry.registerFor(CapabilityTileEntity.class, HEAT_HANDLER_CAPABILITY, (capabilityTileEntity) ->
            new DefaultHeatHandler()
        );
        registry.registerFor(CapabilityTileEntity.class, STRICT_ENERGY_CAPABILITY, (capabilityTileEntity) ->
            new DefaultStrictEnergyHandler()
        );
        registry.registerFor(CapabilityTileEntity.class, CONFIGURABLE_CAPABILITY, (capabilityTileEntity) ->
            new DefaultConfigurable()
        );
        registry.registerFor(CapabilityTileEntity.class, ALLOY_INTERACTION_CAPABILITY, (capabilityTileEntity) ->
            new DefaultAlloyInteraction()
        );
        registry.registerFor(CapabilityTileEntity.class, CONFIG_CARD_CAPABILITY, (capabilityTileEntity) ->
            new DefaultConfigCardAccess()
        );
        registry.registerFor(CapabilityTileEntity.class, EVAPORATION_SOLAR_CAPABILITY, (capabilityTileEntity) ->
            new DefaultEvaporationSolar()
        );
        registry.registerFor(CapabilityTileEntity.class, LASER_RECEPTOR_CAPABILITY, (capabilityTileEntity) ->
            new DefaultLaserReceptor()
        );
        registry.registerFor(CapabilityTileEntity.class, LASER_DISSIPATION_CAPABILITY, (capabilityTileEntity) ->
            new DefaultLaserDissipation()
        );
        registry.registerFor(CapabilityTileEntity.class, RADIATION_SHIELDING_CAPABILITY, (capabilityTileEntity) ->
            new DefaultRadiationShielding()
        );
        registry.registerFor(CapabilityTileEntity.class, RADIATION_ENTITY_CAPABILITY, (capabilityTileEntity) ->
            new DefaultRadiationEntity()
        );
    }

    @Override
    public void registerChunkComponentFactories(@NotNull ChunkComponentFactoryRegistry registry) {

    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {

    }

    @Override
    public void registerItemComponentFactories(@NotNull ItemComponentFactoryRegistry registry) {

    }

    @Override
    public void registerLevelComponentFactories(@NotNull LevelComponentFactoryRegistry registry) {

    }

    @Override
    public void registerScoreboardComponentFactories(@NotNull ScoreboardComponentFactoryRegistry registry) {

    }

    @Override
    public void registerGenericComponentFactories(@NotNull GenericComponentFactoryRegistry registry) {

    }

    @Override
    public void registerWorldComponentFactories(@NotNull WorldComponentFactoryRegistry registry) {

    }

    private static <T extends Component> ComponentKey<T> register(Identifier identifier, Class<T> clazz) {
        return ComponentRegistryV3.INSTANCE.getOrCreate(identifier, clazz);
    }
}
