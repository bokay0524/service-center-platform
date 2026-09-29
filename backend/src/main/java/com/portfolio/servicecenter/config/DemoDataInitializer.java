package com.portfolio.servicecenter.config;

import com.portfolio.servicecenter.dto.ServiceRequestDtos.CreateItemRequest;
import com.portfolio.servicecenter.dto.ServiceRequestDtos.CreateRequest;
import com.portfolio.servicecenter.service.ServiceRequestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("demo")
public class DemoDataInitializer implements CommandLineRunner {
    private final ServiceRequestService service;

    public DemoDataInitializer(ServiceRequestService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.create(new CreateRequest(
                "Kim Demo", "010-1234-5678", "demo1@example.com", "Seoul",
                "SEOUL-01",
                List.of(new CreateItemRequest("Audio", "MODEL-A", "DEMO-SN-001", "No power"))
        ));
        service.create(new CreateRequest(
                "Lee Demo", "010-9876-5432", "demo2@example.com", "Busan",
                "BUSAN-01",
                List.of(new CreateItemRequest("Instrument", "MODEL-B", "DEMO-SN-002", "Intermittent noise"))
        ));
    }
}
