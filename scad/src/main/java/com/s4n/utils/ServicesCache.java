package com.s4n.utils;

import java.util.ArrayList;
import java.util.List;

import com.s4n.delivery.service.OrientationService;
import com.s4n.scad.service.DroneService;

public class ServicesCache {
	private List<DroneService> droneServices = new ArrayList<DroneService>();
	private List<OrientationService> orientationService = new ArrayList<OrientationService>();
	
    public OrientationService getOrientationService(String serviceName) {
        return orientationService.stream().filter(s -> s.getClass().getSimpleName().equalsIgnoreCase(serviceName)).findFirst().orElse(null);
    }
 
    public void addOrientationService(OrientationService newService) {
        this.orientationService.add(newService);
    }
    
    public DroneService getDroneService(String serviceName) {
        return droneServices.stream().filter(s -> s.getClass().getSimpleName().equalsIgnoreCase(serviceName)).findFirst().orElse(null);
    }
 
    public void addDroneService(DroneService newService) {
        this.droneServices.add(newService);
    }
}
