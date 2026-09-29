package com.portfolio.servicecenter.integration;

import com.portfolio.servicecenter.domain.ServiceRequest;

public interface NotificationClient {
    String sendStatusNotification(ServiceRequest request);
}
