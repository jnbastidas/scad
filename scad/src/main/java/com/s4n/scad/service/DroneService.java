package com.s4n.scad.service;

import com.s4n.model.Drone;

public interface DroneService {
	public void startDeliveries(Drone drone, String outFileName);
}
