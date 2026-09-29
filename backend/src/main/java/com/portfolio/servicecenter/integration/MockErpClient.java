package com.portfolio.servicecenter.integration;

import com.portfolio.servicecenter.domain.ServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class MockErpClient implements ErpClient {
    @Override
    public String sendRepairOrder(ServiceRequest request) {
        return "MOCK_ERP_ACCEPTED:" + request.getReferenceNo();
    }
}
