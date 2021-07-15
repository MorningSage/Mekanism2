package mekanism.api.radiation;

import mekanism.api.Coord4D;

import javax.annotation.Nonnull;

/**
 * Interface used for defining radiation sources.
 */
public interface IRadiationSource {

    /**
     * Gets the location of this {@link IRadiationSource}.
     */
    @Nonnull
    Coord4D getPos();

    /**
     * Get the radiation level (in sV/h) of this {@link IRadiationSource}.
     *
     * @return radiation dosage
     */
    double getMagnitude();

    /**
     * Applies a radiation source (Sv) of the given magnitude too this {@link IRadiationSource}.
     *
     * @param magnitude Amount of radiation to apply (Sv).
     */
    void radiate(double magnitude);

    /**
     * Decays the source's radiation level.
     */
    boolean decay();
}