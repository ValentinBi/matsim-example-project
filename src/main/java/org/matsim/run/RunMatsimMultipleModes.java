package org.matsim.run;


import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup.ModeRoutingParams;
import org.matsim.core.config.groups.StrategyConfigGroup.StrategySettings;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule.DefaultStrategy;
import org.matsim.core.scenario.ScenarioUtils;

public class RunMatsimMultipleModes{
	
	public static void main(String[] args) {
		String configfile = "./scenarios/equil/config.xml";
		
		Config config = ConfigUtils.loadConfig(configfile);
		
		config.controler().setOutputDirectory("./output");
		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(2);
		
		{
			StrategySettings stratSets = new StrategySettings();
			stratSets.setStrategyName(DefaultStrategy.ChangeSingleTripMode);
			double probability = 1.0;
			stratSets.setWeight(probability );
			config.strategy().addStrategySettings(stratSets );
			
			String[] modes = {"car", "scooter"};
			config.changeMode().setModes(modes );
		}
		ModeRoutingParams pars = new ModeRoutingParams();
		String mode = "scooter";
		pars.setMode(mode);
		pars.setTeleportedModeFreespeedFactor(1.5);
		// TODO scoring function
		config.plansCalcRoute().addModeRoutingParams(pars );
		Scenario scenario = ScenarioUtils.loadScenario(config);
		Controler controler = new Controler(scenario);
		
		controler.run();
		
	}
}
