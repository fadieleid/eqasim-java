package org.eqasim.core.simulation.modes.drt.mode_choice.constraints;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eqasim.core.simulation.mode_choice.AbstractEqasimExtension;

public class DrtMaxDistanceConstraintModule extends AbstractEqasimExtension {

    public static final String DRT_MAX_DISTANCE_CONSTRAINT_NAME = "DrtMaxDistanceConstraint";

    @Override
    protected void installEqasimExtension() {
        bindTripConstraintFactory(DRT_MAX_DISTANCE_CONSTRAINT_NAME).to(DrtMaxDistanceConstraint.Factory.class);
    }

    @Provides
    @Singleton
    public DrtMaxDistanceConstraint.Factory provideDrtMaxDistanceConstraintFactory(DrtMaxDistanceConstraintConfigGroup configGroup) {
        return new DrtMaxDistanceConstraint.Factory(configGroup.getMaxBeelineDistanceByMode());
    }
}
