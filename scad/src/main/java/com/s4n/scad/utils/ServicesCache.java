package com.s4n.scad.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.s4n.common.model.Drone;
import com.s4n.scad.service.ScadService;

public class ServicesCache {
	private List<ScadService> scadServices = new ArrayList<ScadService>();
	private BlockingQueue<Drone> availableDronesQueue;
	
    public ScadService getScadService(String serviceName) {
        return scadServices.stream().filter(s -> s.getClass().getSimpleName().equalsIgnoreCase(serviceName)).findFirst().orElse(null);
    }
 
    public void addScadService(ScadService newService) {
        this.scadServices.add(newService);
    }

	public BlockingQueue<Drone> getAvailableDronesQueue() {
		return availableDronesQueue;
	}

	public void setAvailableDronesQueue(BlockingQueue<Drone> availableDronesQueue) {
		this.availableDronesQueue = availableDronesQueue;
	}
}
