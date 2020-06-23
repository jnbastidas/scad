package com.s4n.delivery.service;

import java.util.Map;

import com.s4n.model.Orientation;

public interface OrientationService {
	Orientation rotateToRight(Orientation orientation);
	
	Orientation rotateToLeft(Orientation orientation);
	
	void goForward(Orientation initialOrientation, Map<Orientation, Integer> position);
}
