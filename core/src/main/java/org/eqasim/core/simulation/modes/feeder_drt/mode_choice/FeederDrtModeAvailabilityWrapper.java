package org.eqasim.core.simulation.modes.feeder_drt.mode_choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eqasim.core.simulation.modes.feeder_drt.config.FeederDrtConfigGroup;
import org.eqasim.core.simulation.modes.feeder_drt.config.MultiModeFeederDrtConfigGroup;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import org.matsim.contribs.discrete_mode_choice.model.mode_availability.ModeAvailability;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;

/**
 * Wraps a scenario-specific {@link ModeAvailability} and makes each feeder drt
 * mode available whenever both of its underlying modes (the pt mode and the
 * access/egress drt mode) are available according to the delegate.
 * <p>
 * This allows standalone drt (e.g. drt_1) and feeder drt (e.g. feeder_drt_1)
 * to be offered together: the delegate returns the standalone drt mode (for
 * instance through eqasim's additionalAvailableModes), and the feeder mode is
 * derived from it. The wrapper only adds modes. Feeder modes that the delegate
 * already returns (e.g. listed directly in additionalAvailableModes) are kept.
 */
public class FeederDrtModeAvailabilityWrapper implements ModeAvailability {
	private final ModeAvailability delegate;
	private final List<FeederDrtConfigGroup> feederConfigs;

	public FeederDrtModeAvailabilityWrapper(Config config, ModeAvailability delegate) {
		this.delegate = delegate;

		ConfigGroup group = config.getModules().get(MultiModeFeederDrtConfigGroup.GROUP_NAME);
		if (group instanceof MultiModeFeederDrtConfigGroup feederGroup) {
			this.feederConfigs = new ArrayList<>(feederGroup.getModalElements());
		} else {
			this.feederConfigs = Collections.emptyList();
		}
	}

	@Override
	public Collection<String> getAvailableModes(Person person, List<DiscreteModeChoiceTrip> trips) {
		Set<String> modes = new HashSet<>(delegate.getAvailableModes(person, trips));

		for (FeederDrtConfigGroup feederConfig : feederConfigs) {
			if (modes.contains(feederConfig.ptModeName) && modes.contains(feederConfig.accessEgressModeName)) {
				modes.add(feederConfig.mode);
			}
		}

		return modes;
	}
}
