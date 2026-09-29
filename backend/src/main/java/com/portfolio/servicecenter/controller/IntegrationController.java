package com.portfolio.servicecenter.controller;

import com.portfolio.servicecenter.domain.ServiceRequest;
import com.portfolio.servicecenter.integration.DeliveryClient;
import com.portfolio.servicecenter.integration.ErpClient;
import com.portfolio.servicecenter.integration.NotificationClient;
import com.portfolio.servicecenter.service.ServiceRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/integrations")
public class IntegrationController {
    private final ServiceRequestService requestService;
    private final NotificationClient notificationClient;
    private final DeliveryClient deliveryClient;
    private final ErpClient erpClient;

    public IntegrationController(ServiceRequestService requestService,
                                 NotificationClient notificationClient,
                                 DeliveryClient deliveryClient,
                                 ErpClient erpClient) {
        this.requestService = requestService;
        this.notificationClient = notificationClient;
        this.deliveryClient = deliveryClient;
        this.erpClient = erpClient;
    }

    @PostMapping("/{id}/notification")
    public Map<String, String> notification(@PathVariable UUID id) {
        ServiceRequest request = requestService.getEntity(id);
        return Map.of("result", notificationClient.sendStatusNotification(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/erp")
    public Map<String, String> erp(@PathVariable UUID id) {
        ServiceRequest request = requestService.getEntity(id);
        return Map.of("result", erpClient.sendRepairOrder(request));
    }

    @GetMapping("/{id}/delivery")
    public Map<String, String> delivery(@PathVariable UUID id) {
        ServiceRequest request = requestService.getEntity(id);
        return Map.of("result", deliveryClient.getTrackingStatus(request));
    }
}
