package mekanism.api.chemical;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.slurry.Slurry;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Identified;
import net.minecraft.tag.TagGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ChemicalTags<CHEMICAL extends Chemical<CHEMICAL>> {

    public static final ChemicalTags<Gas> GAS = new ChemicalTags<>(new Identifier(MekanismAPI.MEKANISM_MODID, "gas"), MekanismAPI::gasRegistry);
    public static final ChemicalTags<InfuseType> INFUSE_TYPE = new ChemicalTags<>(new Identifier(MekanismAPI.MEKANISM_MODID, "infuse_type"), MekanismAPI::infuseTypeRegistry);
    public static final ChemicalTags<Pigment> PIGMENT = new ChemicalTags<>(new Identifier(MekanismAPI.MEKANISM_MODID, "pigment"), MekanismAPI::pigmentRegistry);
    public static final ChemicalTags<Slurry> SLURRY = new ChemicalTags<>(new Identifier(MekanismAPI.MEKANISM_MODID, "slurry"), MekanismAPI::slurryRegistry);

    private final Supplier<Registry<CHEMICAL>> registrySupplier;
    private final Identifier registryName;

    private ChemicalTags(Identifier registryName, Supplier<Registry<CHEMICAL>> registrySupplier) {
        this.registrySupplier = registrySupplier;
        this.registryName = registryName;
    }


}
