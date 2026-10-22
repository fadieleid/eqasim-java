package org.eqasim.core.simulation.modes.drt.mode_choice.constraints;

import org.matsim.api.core.v01.population.Person;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import org.matsim.contribs.discrete_mode_choice.model.constraints.AbstractTripConstraint;
import org.matsim.contribs.discrete_mode_choice.model.trip_based.TripConstraint;
import org.matsim.contribs.discrete_mode_choice.model.trip_based.TripConstraintFactory;
import org.matsim.core.utils.geometry.CoordUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Forbids a drt mode for trips whose euclidean (beeline) distance between origin and destination activity exceeds
 * the maximum distance configured for that mode in the {@link DrtMaxDistanceConstraintConfigGroup}. Modes without
 * a configured maximum distance (e.g. feeder drt) are always valid.
 */
public class DrtMaxDistanceConstraint extends AbstractTripConstraint {

    private final Map<String, Double> maxDistanceByMode;

    public DrtMaxDistanceConstraint(Map<String, Double> maxDistanceByMode) {
        this.maxDistanceByMode = maxDistanceByMode;
    }

    @Override
    public boolean validateBeforeEstimation(DiscreteModeChoiceTrip trip, String mode, List<String> previousModes) {
        Double maxDistance = maxDistanceByMode.get(mode);
        if(maxDistance != null) {
            return CoordUtils.calcEuclideanDistance(trip.getOriginActivity().getCoord(), trip.getDestinationActivity().getCoord()) <= maxDistance;
        }
        return true;
    }

    public static class Factory implements TripConstraintFactory {

        private final Map<String, Double> maxDistanceByMode;

        public Factory(Map<String, Double> maxDistanceByMode) {
            this.maxDistanceByMode = maxDistanceByMode;
        }

        @Override
        public TripConstraint createConstraint(Person person, List<DiscreteModeChoiceTrip> list, Collection<String> collection) {
            return new DrtMaxDistanceConstraint(maxDistanceByMode);
        }
    }
}
