package com.s4n.delivery.model;

import java.io.Serializable;
import java.util.Map;

public class Flight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Orientation orientation;
	private	Map<Orientation, Integer> position;
	
	public Orientation getOrientation() {
		return orientation;
	}
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	public Map<Orientation, Integer> getPosition() {
		return position;
	}
	public void setPosition(Map<Orientation, Integer> position) {
		this.position = position;
	}
}
