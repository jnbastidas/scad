package com.s4n.delivery.service.impl;

import java.util.Map;

import com.s4n.delivery.service.OrientationService;
import com.s4n.model.Orientation;

public class DefaultOrientationServiceImpl implements OrientationService {
	public static String SERVICE_NAME = "DefaultOrientationServiceImpl";
	
	public void goForward(Orientation initialOrientation, Map<Orientation, Integer> position) {
		if (Orientation.N.equals(initialOrientation)) {
			Integer total = position.get(Orientation.N) + 1;
			position.put(Orientation.N, total);
			
		} else if (Orientation.E.equals(initialOrientation)) {
			Integer total = position.get(Orientation.E) + 1;
			position.put(Orientation.E, total);
			
		} else if (Orientation.S.equals(initialOrientation)) {
			Integer total = position.get(Orientation.S) + 1;
			position.put(Orientation.S, total);
			
		} else {
			Integer total = position.get(Orientation.W) + 1;
			position.put(Orientation.W, total);
		}
	}
	
	public Orientation rotateToRight(Orientation orientation) {
		if (Orientation.N.equals(orientation)) {
			return Orientation.E;
		} else if (Orientation.E.equals(orientation)) {
			return Orientation.S;
		} else if (Orientation.S.equals(orientation)) {
			return Orientation.W;
		} else {
			return Orientation.N;
		}
	}
	
	public Orientation rotateToLeft(Orientation orientation) {
		if (Orientation.N.equals(orientation)) {
			return Orientation.W;
		} else if (Orientation.W.equals(orientation)) {
			return Orientation.S;
		} else if (Orientation.S.equals(orientation)) {
			return Orientation.E;
		} else {
			return Orientation.N;
		}
	}
}
