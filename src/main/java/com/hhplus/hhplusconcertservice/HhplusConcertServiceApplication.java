package com.hhplus.hhplusconcertservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class HhplusConcertServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HhplusConcertServiceApplication.class, args);
    }

}
