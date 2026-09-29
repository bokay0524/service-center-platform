package com.portfolio.servicecenter.integration;

import com.portfolio.servicecenter.domain.ServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class MockNotificationClient implements NotificationClient {
    @Override
    public String sendStatusNotification(ServiceRequest request) {
        return "MOCK_NOTIFICATION_SENT:" + request.getReferenceNo() + ":" + request.getStatus();
    }
}
