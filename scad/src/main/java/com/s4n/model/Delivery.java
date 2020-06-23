package com.s4n.model;

import java.io.Serializable;

public class Delivery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Movement[] path; // For example [ 'A', 'A', 'A', 'D', 'A', 'I', 'A' ]
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Movement[] getPath() {
		return path;
	}
	public void setPath(Movement[] path) {
		this.path = path;
	}

}
