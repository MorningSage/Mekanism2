package mekanism.api.radiation.capability;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * Simple capability that can be added to things like armor to provide shielding against radiation.
 */
public interface IRadiationShielding extends ComponentV3, AutoSyncedComponent {

    /**
     * Gets a percentage representing how much radiation shielding this capability provides.
     *
     * @return Radiation shielding (0.0 to 1.0).
     */
    double getRadiationShielding();
}