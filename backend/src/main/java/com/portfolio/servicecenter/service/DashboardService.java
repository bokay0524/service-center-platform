package com.portfolio.servicecenter.service;

import com.portfolio.servicecenter.domain.ServiceStatus;
import com.portfolio.servicecenter.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {
    private final ServiceRequestRepository repository;

    public DashboardService(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    public Map<String, Long> counts() {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("TOTAL", repository.count());
        for (ServiceStatus status : ServiceStatus.values()) {
            result.put(status.name(), repository.countByStatus(status));
        }
        return result;
    }
}
