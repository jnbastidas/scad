package com.s4n.scad.service;

import java.io.IOException;

import com.s4n.scad.model.DeliveriesFile;

public interface ScadService {
	void processDeliveriesFile(DeliveriesFile deliveriesFile) throws IOException, InterruptedException;
}
