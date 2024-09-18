package com.hhplus.hhplusconcertservice.support.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "resilience4j.circuitbreaker")
public class CircuitBreakerProperty {
    private Float failureRateThreshold = Float.MIN_VALUE;
    private Long slowCallDurationThreshold = Long.MIN_VALUE;
    private Float slowCallRateThreshold = Float.MIN_VALUE;
    private Long waitDurationInOpenState = Long.MIN_VALUE;
    private Integer minimumNumberOfCalls = Integer.MIN_VALUE;
    private Integer slidingWindowSize = Integer.MIN_VALUE;
    private Integer permittedNumberOfCallsInHalfOpenState = Integer.MIN_VALUE;

}
