package com.webflix.webflix.api.v1.filters;

import com.webflix.webflix.services.beans.LogTracerBean;
import com.webflix.webflix.services.config.RestConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
@ApplicationScoped
public class RequestFilter implements ContainerRequestFilter {

	@Inject
	private RestConfig restConfig;

	@Inject
	private LogTracerBean logTracerBean;

	@Override
	public void filter(ContainerRequestContext reqCtx) {
		if (restConfig.getMaintenanceMode()) {
			reqCtx.abortWith(
					Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Service is under maintenance").build()
			);
		} else {
			String uuid;
			if (reqCtx.getHeaders().containsKey("Log-Tracer"))
				uuid = reqCtx.getHeaders().get("Log-Tracer").get(0);
			else
				uuid = UUID.randomUUID().toString();
			logTracerBean.setUuid(uuid);
		}
	}

}
