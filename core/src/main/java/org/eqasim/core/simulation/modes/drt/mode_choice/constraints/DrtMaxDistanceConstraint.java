package org.eqasim.core.simulation.modes.drt.mode_choice.constraints;

import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import org.matsim.contribs.discrete_mode_choice.model.constraints.AbstractTripConstraint;
import org.matsim.contribs.discrete_mode_choice.model.trip_based.TripConstraint;
import org.matsim.contribs.discrete_mode_choice.model.trip_based.TripConstraintFactory;
import org.matsim.core.config.Config;
import org.matsim.core.utils.geometry.CoordUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Forbids a drt mode for trips whose euclidean (beeline) distance between origin and destination activity exceeds
 * a maximum distance. Only the standalone drt modes of the {@link MultiModeDrtConfigGroup} are affected, trips of
 * other modes (e.g. feeder drt) are always valid.
 */
public class DrtMaxDistanceConstraint extends AbstractTripConstraint {

    private final Set<String> drtModes;
    private final double maxDistance_m;

    public DrtMaxDistanceConstraint(Set<String> drtModes, double maxDistance_m) {
        this.drtModes = drtModes;
        this.maxDistance_m = maxDistance_m;
    }

    @Override
    public boolean validateBeforeEstimation(DiscreteModeChoiceTrip trip, String mode, List<String> previousModes) {
        if(drtModes.contains(mode)) {
            return CoordUtils.calcEuclideanDistance(trip.getOriginActivity().getCoord(), trip.getDestinationActivity().getCoord()) <= maxDistance_m;
        }
        return true;
    }

    public static class Factory implements TripConstraintFactory {

        private Set<String> drtModes = new HashSet<>();
        private final double maxDistance_m;

        public Factory(Config config, double maxDistance_m) {
            this.maxDistance_m = maxDistance_m;
            if(config.getModules().containsKey(MultiModeDrtConfigGroup.GROUP_NAME)) {
                MultiModeDrtConfigGroup multiModeDrtConfigGroup = (MultiModeDrtConfigGroup) config.getModules().get(MultiModeDrtConfigGroup.GROUP_NAME);
                drtModes = multiModeDrtConfigGroup.modes().collect(Collectors.toSet());
            }
        }

        @Override
        public TripConstraint createConstraint(Person person, List<DiscreteModeChoiceTrip> list, Collection<String> collection) {
            return new DrtMaxDistanceConstraint(drtModes, maxDistance_m);
        }
    }
}
