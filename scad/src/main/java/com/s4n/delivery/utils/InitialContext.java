package com.s4n.delivery.utils;

import com.s4n.delivery.service.impl.DefaultFlightServiceImpl;

public class InitialContext {
	public Object lookup(String serviceName) {
        if (DefaultFlightServiceImpl.SERVICE_NAME.equalsIgnoreCase(serviceName)) {
            return new DefaultFlightServiceImpl();
        }
        
        return null;
    }
}
