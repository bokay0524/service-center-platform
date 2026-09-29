package com.portfolio.servicecenter.integration;

import com.portfolio.servicecenter.domain.ServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class MockDeliveryClient implements DeliveryClient {
    @Override
    public String getTrackingStatus(ServiceRequest request) {
        return request.getStatus().name().equals("SHIPPED") ? "IN_TRANSIT" : "NOT_SHIPPED";
    }
}
