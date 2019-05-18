package org.matsim.class2019.analysis;

import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.population.Person;

public class TravelTimeEventHandler implements ActivityEndEventHandler, ActivityStartEventHandler{

	private final Map<Id<Person>, Double> travelTimeByAgent = new HashMap<>();
	private final Map<Id<Person>, ActivityEndEvent> openTrips = new HashMap<>();
	
	Map<Id<Person>, Double> getTravelTimeByAgent() {
		return travelTimeByAgent;
	}
	
	double computeOverallTravelTime() {
		return travelTimeByAgent.values().stream().mapToDouble(d -> d).sum();
	}
	@Override
	public void handleEvent(ActivityStartEvent event) {
		// TODO Auto-generated method stub
		if (isNotInteraction(event.getActType()) && (openTrips.containsKey(event.getPersonId()))) {
			ActivityEndEvent endEvent = openTrips.remove(event.getPersonId());
			double travelTime = event.getTime() - endEvent.getTime();
			travelTimeByAgent.merge(event.getPersonId(), travelTime, (a, b) -> a + b);
		}
	}

	@Override
	public void handleEvent(ActivityEndEvent event) {
		if (isNotInteraction(event.getActType())) {
			openTrips.put(event.getPersonId(), event);
			
		}
		
	}
	
	private boolean isNotInteraction(String activityType) {
		return !activityType.contains(" interaction");
	}

}
