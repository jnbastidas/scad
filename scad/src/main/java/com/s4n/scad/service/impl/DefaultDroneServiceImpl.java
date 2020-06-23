package com.s4n.scad.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import com.s4n.delivery.service.impl.FlightSimulatorService;
import com.s4n.model.Delivery;
import com.s4n.model.Drone;
import com.s4n.model.Movement;
import com.s4n.model.Orientation;
import com.s4n.scad.service.DroneService;
import com.s4n.utils.ProjectProperties;

public class DefaultDroneServiceImpl implements DroneService {
	public static String SERVICE_NAME = "DefaultDroneServiceImpl";

	private ProjectProperties projectProperties = ProjectProperties.getInstance();
	
	@Override
	public void startDeliveries(Drone drone, String outFileName) {
		this.validateDrone(drone);
		
		List<Delivery> deliveries = drone.getDeliveries();
		
		//The drone always starts in the (0,0) position
		Map<Orientation, Integer> initialPosition = new HashMap<Orientation, Integer>();
		initialPosition.put(Orientation.N, 0);
		initialPosition.put(Orientation.E, 0);
		initialPosition.put(Orientation.S, 0);
		initialPosition.put(Orientation.W, 0);
		
		//The drone always starts in the North orientation
		Orientation initialOrientation = Orientation.N;
		
		// Create a stop for each delivery
		List<Movement[]> stops = new ArrayList<Movement[]>();
		deliveries.forEach(d -> {
			stops.add(d.getPath());
		});
		
		//Setup the flight
		FlightSimulatorService droneFlightSimulator = new FlightSimulatorService();
		droneFlightSimulator.setInitialPosition(initialPosition);
		droneFlightSimulator.setInitialOrientation(initialOrientation);
		droneFlightSimulator.setStops(stops);
		droneFlightSimulator.setFileOutName(outFileName);
		
		// Star the flight
		Thread flightThread = new Thread(droneFlightSimulator);
		flightThread.start();
	}
	
	private void validateDrone(Drone drone) {
		Integer maxDeliveriesPerDrone = this.projectProperties.getMaxDeliveriesPerDrone();
		
		if (Objects.isNull(drone) || CollectionUtils.isEmpty(drone.getDeliveries()) || drone.getDeliveries().size() > maxDeliveriesPerDrone) {
			throw new IllegalArgumentException("Cannot be over "+maxDeliveriesPerDrone+" deliveries per drone.");
		}
	}
}
