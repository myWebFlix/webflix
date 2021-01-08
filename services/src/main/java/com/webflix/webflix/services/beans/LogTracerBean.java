package com.webflix.webflix.services.beans;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class LogTracerBean {
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
