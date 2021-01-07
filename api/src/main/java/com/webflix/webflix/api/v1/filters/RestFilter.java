package com.webflix.webflix.api.v1.filters;

import com.webflix.webflix.services.config.RestConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class RestFilter implements ContainerRequestFilter {

	@Inject
	private RestConfig restConfig;

	@Override
	public void filter(ContainerRequestContext reqCtx) {
		if (restConfig.getMaintenanceMode()) {
			reqCtx.abortWith(
					Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Service is under maintenance").build()
			);
		}
	}
}
