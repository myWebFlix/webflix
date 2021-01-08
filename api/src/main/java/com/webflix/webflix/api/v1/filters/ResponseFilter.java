package com.webflix.webflix.api.v1.filters;

import com.webflix.webflix.services.beans.LogTracerBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ResponseFilter implements ContainerResponseFilter {

	@Inject
	private LogTracerBean logTracerBean;

	@Override
	public void filter(ContainerRequestContext reqCtx, ContainerResponseContext resCtx) {
		resCtx.getHeaders().add("Log-Tracer", logTracerBean.getUuid());
	}
}
