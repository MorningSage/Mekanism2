package mekanism.common.config;

import mekanism.common.Mekanism;
import mekanism.common.util.ConfigUtils;

public class MekanismConfig {

    private MekanismConfig() {
    }

    public static final GearConfig gear = new GearConfig(Mekanism.MODID);

    public static void init() {
        ConfigUtils.openConfig(gear);
    }

    //public static void registerConfigs(ModLoadingContext modLoadingContext) {
    //    MekanismConfigHelper.registerConfig(modContainer, gear);
    //}
}
