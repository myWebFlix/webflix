package com.fri.webflix.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class RestConfig {

	@ConfigValue(watch = true)
	private boolean maintenanceMode;

	public boolean getMaintenanceMode() {
		return maintenanceMode;
	}

	public void setMaintenanceMode(boolean maintenanceMode) {
		this.maintenanceMode = maintenanceMode;
	}
}
