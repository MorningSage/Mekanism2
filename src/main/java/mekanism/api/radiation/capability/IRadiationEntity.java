package mekanism.api.radiation.capability;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mekanism.api.toport.INBTSerializable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import javax.annotation.Nonnull;

/**
 * Base capability definition for handling radiation for entities.
 */
public interface IRadiationEntity extends INBTSerializable<NbtCompound>, ComponentV3, AutoSyncedComponent {

    /**
     * Gets the radiation dosage (Sv) of the entity.
     *
     * @return radiation dosage
     */
    double getRadiation();

    /**
     * Applies an additional magnitude of radiation to the entity (Sv).
     *
     * @param magnitude dosage of radiation to apply (Sv)
     */
    //void radiate(double magnitude);

    /**
     * Decays the entity's radiation dosage.
     */
    //void decay();

    /**
     * Applies radiation effects to the entity, and syncs the capability if needed.
     */
    //void update(@Nonnull LivingEntity entity);

    /**
     * Sets the radiation level of the entity to a new value.
     *
     * @param magnitude value to set radiation dosage to
     */
    void set(double magnitude);
}
