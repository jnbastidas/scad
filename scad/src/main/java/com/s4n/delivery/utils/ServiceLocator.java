package com.s4n.delivery.utils;

import com.s4n.delivery.service.FlightService;

public class ServiceLocator {
	private static ServicesCache cache = new ServicesCache();
    
    public static FlightService getFlightService(String serviceName) {
    	 
    	FlightService serviceInstance = cache.getFlightService(serviceName);
 
        if (serviceInstance != null) {
            return serviceInstance;
        }
 
        InitialContext context = new InitialContext();
        FlightService newService = (FlightService) context.lookup(serviceName);
        cache.addFlightService(newService);
        return newService;
    }
}
