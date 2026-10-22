package org.eqasim.bavaria;

import java.util.HashSet;
import java.util.Set;

import org.eqasim.core.simulation.EqasimConfigurator;
import org.eqasim.bavaria.mode_choice.BavariaModeChoiceModule;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contribs.discrete_mode_choice.modules.config.DiscreteModeChoiceConfigGroup;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.Config;

public class BavariaConfigurator extends EqasimConfigurator {
	public BavariaConfigurator(CommandLine cmd) {
		super(cmd);

		registerModule(new BavariaModeChoiceModule(cmd));
	}

	@Override
	public void updateConfig(Config config) {
		super.updateConfig(config);

		// The drt distance limit itself is set via the mode choice parameter
		// bavariaDrt.maxBeelineDistance_km (infinite by default, i.e. no limit)
		DiscreteModeChoiceConfigGroup dmcConfig = (DiscreteModeChoiceConfigGroup) config.getModules()
				.get(DiscreteModeChoiceConfigGroup.GROUP_NAME);

		if (dmcConfig != null && config.getModules().containsKey(MultiModeDrtConfigGroup.GROUP_NAME)) {
			Set<String> tripConstraints = new HashSet<>(dmcConfig.getTripConstraints());
			tripConstraints.add(BavariaModeChoiceModule.DRT_MAX_DISTANCE_CONSTRAINT_NAME);
			dmcConfig.setTripConstraints(tripConstraints);
		}
	}
}
