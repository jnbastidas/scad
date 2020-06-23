package com.s4n.delivery.service;

import com.s4n.delivery.model.Flight;

public interface FlightService {

	void rotateToRight(Flight flight);
	
	void rotateToLeft(Flight flight);
	
	void goForward(Flight flight);

}
