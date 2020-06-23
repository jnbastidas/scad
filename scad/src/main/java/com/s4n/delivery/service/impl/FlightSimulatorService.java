package com.s4n.delivery.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.s4n.delivery.service.OrientationService;
import com.s4n.model.Movement;
import com.s4n.model.Orientation;
import com.s4n.utils.ServiceLocator;

public class FlightSimulatorService implements Runnable {
	private OrientationService orientationService = ServiceLocator.getOrientationService(DefaultOrientationServiceImpl.SERVICE_NAME);

	private Map<Orientation, Integer> initialPosition;
	private Orientation initialOrientation;
	private List<Movement[]> stops;
	private String fileOutName;

	@Override
	public void run() {
		Map<Orientation, Integer> realTimePosition = initialPosition;
		Orientation realTimeOrientation = initialOrientation;
		
		writeFlatFile(fileOutName, "== Deliveries Summary ==");
		for (Movement[] movements : stops) {
			realTimeOrientation = this.exeuteMovements(realTimeOrientation, movements, realTimePosition);

			Integer x = realTimePosition.get(Orientation.E) - realTimePosition.get(Orientation.W);
			Integer y = realTimePosition.get(Orientation.N) - realTimePosition.get(Orientation.S);

			writeFlatFile(fileOutName, "(" + x + ", " + y + ") " + realTimeOrientation.getDescription());
		}
	}

	private Orientation exeuteMovements(Orientation initialOrientation, Movement[] movements,
			Map<Orientation, Integer> position) {
		for (Movement movement : movements) {
			initialOrientation = this.exeuteMovement(movement, initialOrientation, position);
		}

		return initialOrientation;
	}

	private Orientation exeuteMovement(Movement movement, Orientation orientation, Map<Orientation, Integer> position) {

		if (Movement.A.equals(movement)) {
			this.orientationService.goForward(orientation, position);
		} else if (Movement.D.equals(movement)) {
			orientation = this.orientationService.rotateToRight(orientation);
		} else {
			orientation = this.orientationService.rotateToLeft(orientation);
		}

		return orientation;
	}

	public static void writeFlatFile(String fileOutName, String line) {
		try {
			FileWriter fw = new FileWriter("src/main/resources/" + fileOutName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(line);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occurred writting file " + fileOutName);
			e.printStackTrace();
		}
	}

	public Orientation getInitialOrientation() {
		return initialOrientation;
	}

	public void setInitialOrientation(Orientation initialOrientation) {
		this.initialOrientation = initialOrientation;
	}

	public List<Movement[]> getStops() {
		return stops;
	}

	public void setStops(List<Movement[]> stops) {
		this.stops = stops;
	}

	public Map<Orientation, Integer> getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(Map<Orientation, Integer> initialPosition) {
		this.initialPosition = initialPosition;
	}

	public String getFileOutName() {
		return fileOutName;
	}

	public void setFileOutName(String fileOutName) {
		this.fileOutName = fileOutName;
	}

}
