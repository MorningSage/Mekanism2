package mekanism.api.chemical.infuse;

import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public final class EmptyInfuseType extends InfuseType {

    public EmptyInfuseType() {
        super(InfuseTypeBuilder.builder().hidden());
    }

    @Override
    public boolean isIn(@Nonnull Tag<InfuseType> tags) {
        //Empty infuse type is in no tags
        return false;
    }

}
