package org.matsim.class2019.analysis;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;

public class RunAnalysis {
	 public static void main(String[] args) {
		 Path events = (Path) Paths.get("/home/valentin/MATSim/Analysis-Tut/base_case/output_events.xml.gz");
		 Path policyEvents = (Path) Paths.get("/home/valentin/MATSim/Analysis-Tut/policy_case/output_events.xml.gz");
		 
		 TravelTimeEventHandler handler = new TravelTimeEventHandler();
		 EventsManager manager = EventsUtils.createEventsManager();
		 manager.addHandler(handler);
		 
		 new MatsimEventsReader(manager).readFile(events.toString());
		 
		 double totalTravelTime = handler.computeOverallTravelTime();
		 
		 //Policy case:
		 TravelTimeEventHandler policyHandler = new TravelTimeEventHandler();
		 EventsManager policyManager = EventsUtils.createEventsManager();
		 policyManager.addHandler(policyHandler);
		 
		 new MatsimEventsReader(policyManager).readFile(policyEvents.toString());
		 
		 double totalTravelTimePolicy = policyHandler.computeOverallTravelTime();
		 System.out.println("total travel time :"+totalTravelTime);

		 System.out.println("total policy travel time :"+totalTravelTimePolicy);

		 System.out.println("difference total travel time :"+(totalTravelTimePolicy-totalTravelTime));
	 }
}
