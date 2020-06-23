package com.s4n.utils;

import com.s4n.delivery.service.impl.DefaultOrientationServiceImpl;
import com.s4n.scad.service.impl.DefaultDroneServiceImpl;

public class InitialContext {
	public Object lookup(String serviceName) {
        if (DefaultOrientationServiceImpl.SERVICE_NAME.equalsIgnoreCase(serviceName)) {
            return new DefaultOrientationServiceImpl();
        }
        
        if (DefaultDroneServiceImpl.SERVICE_NAME.equalsIgnoreCase(serviceName)) {
            return new DefaultDroneServiceImpl();
        }
        
        return null;
    }
}
