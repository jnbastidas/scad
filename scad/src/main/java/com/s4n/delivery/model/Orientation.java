package com.s4n.delivery.model;

public enum Orientation {
	N("North Direction"), S("South Direction"), E("East Direction"), W("West Direction");
	
	private String description;
	
	private Orientation(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
