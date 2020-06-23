package com.s4n.scad.utils;

import com.s4n.scad.service.impl.DefaultScadServiceImpl;

public class InitialContext {
	public Object lookup(String serviceName) {
        if (DefaultScadServiceImpl.SERVICE_NAME.equalsIgnoreCase(serviceName)) {
            return new DefaultScadServiceImpl();
        }
        
        return null;
    }
}
