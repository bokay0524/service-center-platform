package com.portfolio.servicecenter.repository;

import com.portfolio.servicecenter.domain.ServiceRequest;
import com.portfolio.servicecenter.domain.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {
    long countByStatus(ServiceStatus status);
    List<ServiceRequest> findAllByOrderByCreatedAtDesc();
    List<ServiceRequest> findByCreatedAtBeforeAndPrivacyMaskedFalse(LocalDateTime cutoff);
}
