package org.eqasim.ile_de_france;

import org.eqasim.core.scenario.validation.VehiclesValidator;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.drt.routing.DrtRoute;
import org.matsim.contrib.drt.routing.DrtRouteFactory;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.eqasim.ile_de_france.mode_choice.IDFModeChoiceModule;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;
import org.eqasim.core.components.transit.EqasimTransitQSimModule;
import org.eqasim.core.components.config.EqasimConfigGroup;

public class RunSimulation {
	static public void main(String[] args) throws ConfigurationException {
		CommandLine cmd = new CommandLine.Builder(args) //
				.requireOptions("config-path") //
				.allowPrefixes("mode-choice-parameter", "cost-parameter") //
				.build();

		IDFConfigurator configurator = new IDFConfigurator(cmd);
		Config config = ConfigUtils.loadConfig(cmd.getOptionStrict("config-path"));
		configurator.updateConfig(config);

		cmd.applyConfiguration(config);
		VehiclesValidator.validate(config);

		Scenario scenario = ScenarioUtils.createScenario(config);
		configurator.configureScenario(scenario);

		{ // Add DRT route factory
			scenario.getPopulation().getFactory().getRouteFactories().setRouteFactory(DrtRoute.class,
					new DrtRouteFactory());
		}

		ScenarioUtils.loadScenario(scenario);
		configurator.adjustScenario(scenario);

		Controler controller = new Controler(scenario);
		configurator.configureController(controller);

		{ // Configure controller for DRT
			MultiModeDrtConfigGroup multiModeDrtConfig = MultiModeDrtConfigGroup.get(config);
			controller.configureQSimComponents(components -> {
				DvrpQSimComponents.activateAllModes(multiModeDrtConfig).configure(components);
				
				// Configure transit module
				EqasimConfigGroup eqasimConfig = EqasimConfigGroup.get(config);
				eqasimConfig.setUseScheduleBasedTransport(true);
				EqasimTransitQSimModule.configure(components, config);
			});
		}

		{ // Add overrides for DRT
			controller.addOverridingModule(new IDFModeChoiceModule(cmd));
		}


		controller.run();
	}
}