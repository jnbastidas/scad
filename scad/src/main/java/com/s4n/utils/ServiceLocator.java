package com.s4n.utils;

import com.s4n.delivery.service.OrientationService;
import com.s4n.scad.service.DroneService;

public class ServiceLocator {
	private static ServicesCache cache = new ServicesCache();
	
	public static DroneService getDroneService(String serviceName) {
   	 
		DroneService serviceInstance = cache.getDroneService(serviceName);
 
        if (serviceInstance != null) {
            return serviceInstance;
        }
 
        InitialContext context = new InitialContext();
        DroneService newService = (DroneService) context.lookup(serviceName);
        cache.addDroneService(newService);
        return newService;
    }
    
    public static OrientationService getOrientationService(String serviceName) {
    	 
    	OrientationService serviceInstance = cache.getOrientationService(serviceName);
 
        if (serviceInstance != null) {
            return serviceInstance;
        }
 
        InitialContext context = new InitialContext();
        OrientationService newService = (OrientationService) context.lookup(serviceName);
        cache.addOrientationService(newService);
        return newService;
    }
}
