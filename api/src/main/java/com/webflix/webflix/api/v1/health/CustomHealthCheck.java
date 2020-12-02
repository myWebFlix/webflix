package com.webflix.webflix.api.v1.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.webflix.webflix.services.config.RestConfig;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class CustomHealthCheck implements HealthCheck {

    @Inject
    private RestConfig restConfig;

    @Override
    public HealthCheckResponse call() {
        if (restConfig.getBroken()) {
            return HealthCheckResponse.down(CustomHealthCheck.class.getSimpleName());
        }
        else {
            return HealthCheckResponse.up(CustomHealthCheck.class.getSimpleName());
        }
    }
}
