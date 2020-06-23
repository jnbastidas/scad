package com.s4n.delivery.frontgateway;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s4n.common.model.Delivery;
import com.s4n.common.model.Drone;
import com.s4n.common.model.Movement;
import com.s4n.delivery.model.Flight;
import com.s4n.delivery.model.Orientation;
import com.s4n.delivery.service.FlightService;
import com.s4n.delivery.service.impl.DefaultFlightServiceImpl;
import com.s4n.delivery.utils.ServiceLocator;

public class FlightSimulator implements Runnable {
	private FlightService flightService = ServiceLocator.getFlightService(DefaultFlightServiceImpl.SERVICE_NAME);

	private Drone drone;
	private String outputReportId;

	@Override
	public void run() {
		//A new flight always starts in the (0,0) position
		Map<Orientation, Integer> initialPosition = new HashMap<Orientation, Integer>();
		initialPosition.put(Orientation.N, 0);
		initialPosition.put(Orientation.E, 0);
		initialPosition.put(Orientation.S, 0);
		initialPosition.put(Orientation.W, 0);
		
		//A new flight always starts in the North orientation
		Orientation initialOrientation = Orientation.N;
		
		Flight flight =  new Flight();
		flight.setOrientation(initialOrientation);
		flight.setPosition(initialPosition);
		
		writeFlatFile(outputReportId, "== Deliveries Summary Drone "+drone.getName()+" ==");
		for (Delivery delivery : drone.getDeliveries()) {
			Movement[] movements = delivery.getPath();
			this.exeuteMovementsOnFlight(movements, flight);

			Integer x = flight.getPosition().get(Orientation.E) - flight.getPosition().get(Orientation.W);
			Integer y = flight.getPosition().get(Orientation.N) - flight.getPosition().get(Orientation.S);

			writeFlatFile(outputReportId, "(" + x + ", " + y + ") " + flight.getOrientation().getDescription());
		}
	}

	private void exeuteMovementsOnFlight(Movement[] movements, Flight flight) {
		for (Movement movement : movements) {
			this.exeuteMovementOnFlight(movement, flight);
		}
	}

	private void exeuteMovementOnFlight(Movement movement, Flight flight) {
		if (Movement.A.equals(movement)) {
			this.flightService.goForward(flight);
		} else if (Movement.D.equals(movement)) {
			this.flightService.rotateToRight(flight);
		} else {
			this.flightService.rotateToLeft(flight);
		}
	}

	public static void writeFlatFile(String fileOutName, String line) {
		try {
			FileWriter fw = new FileWriter("src/main/resources/out/" + fileOutName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(line);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occurred writting file " + fileOutName);
			e.printStackTrace();
		}
	}

	public Drone getDrone() {
		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public String getOutputReportId() {
		return outputReportId;
	}

	public void setOutputReportId(String outputReportId) {
		this.outputReportId = outputReportId;
	}
}
