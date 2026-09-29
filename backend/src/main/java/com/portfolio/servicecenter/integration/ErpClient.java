package com.portfolio.servicecenter.integration;

import com.portfolio.servicecenter.domain.ServiceRequest;

public interface ErpClient {
    String sendRepairOrder(ServiceRequest request);
}
