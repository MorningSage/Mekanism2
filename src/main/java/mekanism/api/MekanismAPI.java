package mekanism.api;

import mekanism.api.chemical.gas.EmptyGas;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.infuse.EmptyInfuseType;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.pigment.EmptyPigment;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.slurry.EmptySlurry;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.ModuleData;
import mekanism.api.radiation.IRadiationManager;
import mekanism.api.util.RegistryUtils;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class MekanismAPI {

    private MekanismAPI() {
    }

    /**
     * The version of the api classes - may not always match the mod's version
     */
    public static final String API_VERSION = "10.1.0";
    public static final String MEKANISM_MODID = "mekanism";
    /**
     * Mekanism debug mode
     */
    public static boolean debug = false;

    public static final Logger logger = LogManager.getLogger(MEKANISM_MODID + "_api");

    private static Registry<Gas> GAS_REGISTRY;
    private static Registry<InfuseType> INFUSE_TYPE_REGISTRY;
    private static Registry<Pigment> PIGMENT_REGISTRY;
    private static Registry<Slurry> SLURRY_REGISTRY;
    private static Registry<ModuleData<?>> MODULE_REGISTRY;
    private static IRadiationManager RADIATION_MANAGER;
    private static IModuleHelper MODULE_HELPER;

    //Note: None of the empty variants support registry replacement
    //TODO - 1.17: Rename registry names for the empty types to just being mekanism:empty instead of mekanism:empty_type,
    // and also potentially define these with ObjectHolder for purposes of fully defining them outside of the API
    /**
     * Empty Gas instance.
     */
    @Nonnull
    public static final Gas EMPTY_GAS = new EmptyGas();
    /**
     * Empty Infuse Type instance.
     */
    @Nonnull
    public static final InfuseType EMPTY_INFUSE_TYPE = new EmptyInfuseType();
    /**
     * Empty Pigment instance.
     */
    @Nonnull
    public static final Pigment EMPTY_PIGMENT = new EmptyPigment();
    /**
     * Empty Slurry instance.
     */
    @Nonnull
    public static final Slurry EMPTY_SLURRY = new EmptySlurry();

    /**
     * Gets the Registry for {@link Gas}.
     */
    @Nonnull
    public static Registry<Gas> gasRegistry() {
        if (GAS_REGISTRY == null) {
            GAS_REGISTRY = RegistryUtils.registerRegistry(
                Gas.class, new Identifier(MekanismAPI.MEKANISM_MODID, "gases")
            );
        }
        return GAS_REGISTRY;
    }

    /**
     * Gets the Registry for {@link InfuseType}.
     */
    @Nonnull
    public static Registry<InfuseType> infuseTypeRegistry() {
        if (INFUSE_TYPE_REGISTRY == null) {
            INFUSE_TYPE_REGISTRY = RegistryUtils.registerRegistry(
                InfuseType.class, new Identifier(MekanismAPI.MEKANISM_MODID, "infuse_types")
            );
        }
        return INFUSE_TYPE_REGISTRY;
    }

    /**
     * Gets the Registry for {@link Pigment}.
     */
    @Nonnull
    public static Registry<Pigment> pigmentRegistry() {
        if (PIGMENT_REGISTRY == null) {
            PIGMENT_REGISTRY = RegistryUtils.registerRegistry(
                Pigment.class, new Identifier(MekanismAPI.MEKANISM_MODID, "pigments")
            );
        }
        return PIGMENT_REGISTRY;
    }

    /**
     * Gets the Registry for {@link Slurry}.
     */
    @Nonnull
    public static Registry<Slurry> slurryRegistry() {
        if (SLURRY_REGISTRY == null) {
            SLURRY_REGISTRY = RegistryUtils.registerRegistry(
                Slurry.class, new Identifier(MekanismAPI.MEKANISM_MODID, "slurries")
            );
        }
        return SLURRY_REGISTRY;
    }

    /**
     * Gets the Registry for {@link ModuleData}.
     */
    @Nonnull
    public static Registry<ModuleData<?>> moduleRegistry() {
        if (MODULE_REGISTRY == null) {
            MODULE_REGISTRY = RegistryUtils.registerRegistry(
                ModuleData.getClassWithGeneric(), new Identifier(MekanismAPI.MEKANISM_MODID, "module_data")
            );
        }
        return MODULE_REGISTRY;
    }

    /**
     * Gets Mekanism's {@link IModuleHelper} that provides various utility methods for implementing custom modules.
     */
    public static IModuleHelper getModuleHelper() {
        // Harmless race
        if (MODULE_HELPER == null) {
            try {
                Class<?> clazz = Class.forName("mekanism.common.content.gear.ModuleHelper");
                MODULE_HELPER = (IModuleHelper) clazz.getField("INSTANCE").get(null);
            } catch (ReflectiveOperationException ex) {
                logger.fatal("Error retrieving RadiationManager, Mekanism may be absent, damaged, or outdated.");
            }
        }
        return MODULE_HELPER;
    }

    /**
     * Gets Mekanism's {@link IRadiationManager}.
     */
    public static IRadiationManager getRadiationManager() {
        // Harmless race
        if (RADIATION_MANAGER == null) {
            try {
                Class<?> clazz = Class.forName("mekanism.common.lib.radiation.RadiationManager");
                RADIATION_MANAGER = (IRadiationManager) clazz.getField("INSTANCE").get(null);
            } catch (ReflectiveOperationException ex) {
                logger.fatal("Error retrieving RadiationManager, Mekanism may be absent, damaged, or outdated.");
            }
        }
        return RADIATION_MANAGER;
    }
}
