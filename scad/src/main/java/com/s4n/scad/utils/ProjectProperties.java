package com.s4n.scad.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectProperties {
	private static ProjectProperties INSTANCE;
	private final Properties properties;
	
	private static final String MAX_DELIVERIES_PER_DRON = "deliveries.max";
	private static final String MAX_BLOCKS_TO_DELIVERY = "blocks.max";
	
    public static ProjectProperties getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ProjectProperties();
        }
         
        return INSTANCE;
    }

    private ProjectProperties() {
        this.properties = new Properties();
        try {
            this.properties.load(getClass().getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public Integer getMaxDeliveriesPerDrone() {
        String maxDeliveries = this.properties.getProperty(MAX_DELIVERIES_PER_DRON);
        return getIntegerFronString(maxDeliveries);
    }
    
    public Integer getMaxBlock() {
    	String maxBlocks = this.properties.getProperty(MAX_BLOCKS_TO_DELIVERY);
        return getIntegerFronString(maxBlocks);
    }
    
    private Integer getIntegerFronString(String number) {
    	Integer  rdo = null;
    	
    	try {
        	rdo = Integer.valueOf(number);
        } catch (NumberFormatException e) {
        	Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
        
        return rdo;
    }
}
