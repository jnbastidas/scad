package com.s4n.scad.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.s4n.common.model.Drone;
import com.s4n.scad.service.ScadService;

public class ServiceLocator {
	private static ServicesCache cache = new ServicesCache();
	private static Integer MAX_DRONES = 20;
	
	public static ScadService getScadService(String serviceName) {
   	 
		ScadService serviceInstance = cache.getScadService(serviceName);
 
        if (serviceInstance != null) {
            return serviceInstance;
        }
 
        InitialContext context = new InitialContext();
        ScadService newService = (ScadService) context.lookup(serviceName);
        cache.addScadService(newService);
        return newService;
    }
	
	public static BlockingQueue<Drone> getAvailableDronesQueue() {
		BlockingQueue<Drone> blockingQueue = cache.getAvailableDronesQueue();
		 
        if (blockingQueue != null) {
            return blockingQueue;
        }
 
        BlockingQueue<Drone> newBlockingQueue = new LinkedBlockingQueue<>(MAX_DRONES);
        cache.setAvailableDronesQueue(newBlockingQueue);
        return newBlockingQueue;
	}
}
