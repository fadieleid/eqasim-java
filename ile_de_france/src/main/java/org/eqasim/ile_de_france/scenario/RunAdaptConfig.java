package org.eqasim.ile_de_france.scenario;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eqasim.core.components.config.ConfigAdapter;
import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.termination.EqasimTerminationConfigGroup;
import org.eqasim.ile_de_france.IDFConfigurator;
import org.eqasim.ile_de_france.mode_choice.IDFModeChoiceModule;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contribs.discrete_mode_choice.modules.config.DiscreteModeChoiceConfigGroup;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup.VehiclesSource;
import org.matsim.core.config.groups.VehiclesConfigGroup;
import org.matsim.core.config.groups.RoutingConfigGroup.TeleportedModeParams;
import org.matsim.core.config.groups.ScoringConfigGroup;
import org.matsim.core.config.groups.ScoringConfigGroup.ActivityParams;
import org.matsim.core.config.groups.ScoringConfigGroup.ModeParams;

public class RunAdaptConfig {
	static public void main(String[] args) throws ConfigurationException {
		CommandLine cmd = new CommandLine.Builder(args).allowAnyOption(true).build();
		ConfigAdapter.run(args, new IDFConfigurator(cmd), RunAdaptConfig::adaptConfiguration);
	}

	static public void adaptConfiguration(Config config, String prefix) {
		// Adjust eqasim config
		EqasimConfigGroup eqasimConfig = EqasimConfigGroup.get(config);

		eqasimConfig.setCostModel(TransportMode.car, IDFModeChoiceModule.CAR_COST_MODEL_NAME);
		eqasimConfig.setCostModel(TransportMode.pt, IDFModeChoiceModule.PT_COST_MODEL_NAME);
		eqasimConfig.setCostModel(TransportMode.drt, "ZeroCostModel");

		eqasimConfig.setEstimator(TransportMode.car, IDFModeChoiceModule.CAR_ESTIMATOR_NAME);
		eqasimConfig.setEstimator(TransportMode.bike, IDFModeChoiceModule.BIKE_ESTIMATOR_NAME);
		eqasimConfig.setEstimator(TransportMode.drt, IDFModeChoiceModule.DRT_ESTIMATOR_NAME);
		eqasimConfig.setEstimator("bicycle", "ZeroUtilityEstimator");

		DiscreteModeChoiceConfigGroup dmcConfig = (DiscreteModeChoiceConfigGroup) config.getModules()
				.get(DiscreteModeChoiceConfigGroup.GROUP_NAME);

		dmcConfig.setModeAvailability(IDFModeChoiceModule.MODE_AVAILABILITY_NAME);
		dmcConfig.setSelector("MultinomialLogit");
		// Calibration results for 5%

		// Set up choice model
		eqasimConfig.setCostModel("drt", "ZeroCostModel");
		eqasimConfig.setEstimator("drt", "DrtUtilityEstimator");

		// Add bicycle to cached modes
		Set<String> cachedModes = new HashSet<>(dmcConfig.getCachedModes());
		cachedModes.add("bicycle");
		dmcConfig.setCachedModes(cachedModes);

		// Add bicycle to teleported modes
		TeleportedModeParams bicycleParams = config.routing().getOrCreateModeRoutingParams("bicycle");
		bicycleParams.setBeelineDistanceFactor(1.4);
		bicycleParams.setTeleportedModeSpeed(4.166666666666667);

		// Configure VehicleContinuity constraint
		dmcConfig.getVehicleTourConstraintConfig().setRestrictedModes(Arrays.asList("bicycle", "car"));

		// Configure trip constraints
		dmcConfig.setTripConstraints(Arrays.asList("DrtWalkConstraint", "OutsideConstraint", "TransitWalk"));

		// Add DRT to cached modes
		cachedModes = new HashSet<>(dmcConfig.getCachedModes());
		cachedModes.add("drt");
		dmcConfig.setCachedModes(cachedModes);

		if (eqasimConfig.getSampleSize() == 0.05) {
			// Adjust flow and storage capacity
			config.qsim().setFlowCapFactor(0.045);
			config.qsim().setStorageCapFactor(0.045);
		}

		// Vehicles
		QSimConfigGroup qsimConfig = config.qsim();
		qsimConfig.setVehiclesSource(VehiclesSource.fromVehiclesData);

		VehiclesConfigGroup vehiclesConfig = config.vehicles();
		vehiclesConfig.setVehiclesFile(prefix + "vehicles.xml.gz");

		// Termination settings
		EqasimTerminationConfigGroup terminationConfig = EqasimTerminationConfigGroup.getOrCreate(config);
		terminationConfig.setModes(Arrays.asList("car", "car_passenger", "pt", "bicycle", "walk", "drt"));

		// Scoring config
		ScoringConfigGroup scoringConfig = config.scoring();
		
		// Add bicycle mode parameters
		ModeParams bicycleModeParams = new ModeParams("bicycle");
		bicycleModeParams.setConstant(0.0);
		bicycleModeParams.setMarginalUtilityOfDistance(0.0);
		bicycleModeParams.setMarginalUtilityOfTraveling(-1.0);
		scoringConfig.addModeParams(bicycleModeParams);

		// Add bicycle interaction activity
		ActivityParams bicycleInteractionParams = new ActivityParams("bicycle interaction");
		bicycleInteractionParams.setTypicalDuration(1.0);
		scoringConfig.addActivityParams(bicycleInteractionParams);

		// Add DRT mode parameters
		ModeParams drtModeParams = new ModeParams("drt");
		drtModeParams.setConstant(0.0);
		drtModeParams.setMarginalUtilityOfDistance(0.0);
		drtModeParams.setMarginalUtilityOfTraveling(-0.5); // Make it more attractive than other modes
		scoringConfig.addModeParams(drtModeParams);

		// Add DRT interaction activity
		ActivityParams drtInteractionParams = new ActivityParams("drt interaction");
		drtInteractionParams.setTypicalDuration(1.0);
		scoringConfig.addActivityParams(drtInteractionParams);
	}
}
