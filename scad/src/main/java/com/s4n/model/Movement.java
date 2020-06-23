package com.s4n.model;

public enum Movement {
	A("Go forward"), D("Rotate to right"), I("Rotate to left");
	
	private String description;
	
	private Movement(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
