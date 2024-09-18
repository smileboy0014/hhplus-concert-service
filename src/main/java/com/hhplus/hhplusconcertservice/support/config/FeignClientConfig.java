package com.hhplus.hhplusconcertservice.support.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.hhplus.hhplusconcertservice.domain.client")
@Configuration
public class FeignClientConfig {
}
