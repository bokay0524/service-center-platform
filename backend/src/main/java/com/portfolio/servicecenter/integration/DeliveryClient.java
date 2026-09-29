package com.portfolio.servicecenter.integration;

import com.portfolio.servicecenter.domain.ServiceRequest;

public interface DeliveryClient {
    String getTrackingStatus(ServiceRequest request);
}
