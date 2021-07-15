package mekanism.api.chemical.pigment;

import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public final class EmptyPigment extends Pigment {

    public EmptyPigment() {
        super(PigmentBuilder.builder().hidden());
    }

    @Override
    public boolean isIn(@Nonnull Tag<Pigment> tags) {
        //Empty pigment is in no tags
        return false;
    }

}
