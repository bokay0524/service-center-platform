package com.portfolio.servicecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ServiceCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCenterApplication.class, args);
    }
}
