package com.s4n.delivery.utils;

import java.util.ArrayList;
import java.util.List;

import com.s4n.delivery.service.FlightService;

public class ServicesCache {
	private List<FlightService> flightServices = new ArrayList<FlightService>();
    
    public FlightService getFlightService(String serviceName) {
        return flightServices.stream().filter(s -> s.getClass().getSimpleName().equalsIgnoreCase(serviceName)).findFirst().orElse(null);
    }
 
    public void addFlightService(FlightService newService) {
        this.flightServices.add(newService);
    }
}