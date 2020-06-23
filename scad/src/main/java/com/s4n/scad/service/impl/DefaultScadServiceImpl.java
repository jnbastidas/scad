package com.s4n.scad.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.collections4.CollectionUtils;

import com.s4n.common.model.Delivery;
import com.s4n.common.model.Drone;
import com.s4n.delivery.frontgateway.FlightSimulator;
import com.s4n.scad.model.DeliveriesFile;
import com.s4n.scad.service.ScadService;
import com.s4n.scad.utils.ProjectProperties;
import com.s4n.scad.utils.ServiceLocator;

public class DefaultScadServiceImpl implements ScadService {
	public static String SERVICE_NAME = "DefaultScadServiceImpl";

	private ProjectProperties projectProperties = ProjectProperties.getInstance();
	
	@Override
	public void processDeliveriesFile(DeliveriesFile deliveriesFile) throws IOException, InterruptedException {
		this.validateDeliveries(deliveriesFile.getDeliveries());
		
		String outputReportId = deliveriesFile.getName().replace("in", "out");
		this.createReport(outputReportId);
		
		BlockingQueue<Drone> availableDronesQueue = ServiceLocator.getAvailableDronesQueue();
		Drone drone = availableDronesQueue.take();
		
		System.out.println("Drones avalibles: "+availableDronesQueue.size());
		
		drone.setDeliveries(deliveriesFile.getDeliveries());
		
		this.startFlight(drone, outputReportId);
		
	}
	
	private void validateDeliveries(List<Delivery> deliveries) {
		Integer maxDeliveriesPerDrone = this.projectProperties.getMaxDeliveriesPerDrone();
		
		if (CollectionUtils.isEmpty(deliveries) || deliveries.size() > maxDeliveriesPerDrone) {
			throw new IllegalArgumentException("Cannot be over "+maxDeliveriesPerDrone+" deliveries per drone.");
		}
	}
	
	private void startFlight(Drone drone, String outputReportId) {
		
		//Setup the flight
		FlightSimulator flightSimulator = new FlightSimulator();
		flightSimulator.setDrone(drone);
		flightSimulator.setOutputReportId(outputReportId);
		
		// Star the flight
		Thread flightThread = new Thread(flightSimulator);
		flightThread.start();
	}
	
	private void createReport(String reportId) throws IOException {
		File myObj = new File("src/main/resources/out/" + reportId);
		if(!myObj.createNewFile()) {
			throw new RuntimeException("Error creating ouput report.");
		}
	}
}
