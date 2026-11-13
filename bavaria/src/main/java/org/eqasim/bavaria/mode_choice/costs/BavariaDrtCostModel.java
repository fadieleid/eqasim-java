package org.eqasim.bavaria.mode_choice.costs;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.cost.CostModel;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import org.matsim.core.utils.geometry.CoordUtils;

public class BavariaDrtCostModel implements CostModel {
    private static final double BASE_FARE_EUR = 2.0;
    private static final double PER_KM_EUR = 0.2;

    @Override
    public double calculateCost_MU(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        double distance_km = CoordUtils.calcEuclideanDistance(
                trip.getOriginActivity().getCoord(),
                trip.getDestinationActivity().getCoord()) / 1000.0;
        return BASE_FARE_EUR + PER_KM_EUR * distance_km;
    }
}