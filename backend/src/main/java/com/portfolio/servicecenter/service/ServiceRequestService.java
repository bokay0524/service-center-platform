package com.portfolio.servicecenter.service;

import com.portfolio.servicecenter.domain.ServiceItem;
import com.portfolio.servicecenter.domain.ServiceRequest;
import com.portfolio.servicecenter.domain.ServiceStatus;
import com.portfolio.servicecenter.dto.ServiceRequestDtos.CreateRequest;
import com.portfolio.servicecenter.dto.ServiceRequestDtos.Response;
import com.portfolio.servicecenter.repository.ServiceRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ServiceRequestService {
    private final ServiceRequestRepository repository;

    public ServiceRequestService(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    public List<Response> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream().map(Response::from).toList();
    }

    public Response findById(UUID id) {
        return Response.from(getEntity(id));
    }

    @Transactional
    public Response create(CreateRequest dto) {
        ServiceRequest request = new ServiceRequest();
        request.setReferenceNo(generateReferenceNo());
        request.setCustomerName(dto.customerName());
        request.setCustomerPhone(dto.customerPhone());
        request.setCustomerEmail(dto.customerEmail());
        request.setCustomerAddress(dto.customerAddress());
        request.setBranchCode(dto.branchCode());
        request.setStatus(ServiceStatus.RECEIVED);

        dto.items().forEach(itemDto -> {
            ServiceItem item = new ServiceItem();
            item.setProductCategory(itemDto.productCategory());
            item.setModelName(itemDto.modelName());
            item.setSerialNo(itemDto.serialNo());
            item.setSymptom(itemDto.symptom());
            item.setStatus(ServiceStatus.RECEIVED);
            request.addItem(item);
        });

        return Response.from(repository.save(request));
    }

    @Transactional
    public Response updateStatus(UUID id, ServiceStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        ServiceRequest request = getEntity(id);
        request.setStatus(status);
        request.getItems().forEach(item -> item.setStatus(status));
        return Response.from(request);
    }

    public ServiceRequest getEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service request not found: " + id));
    }

    private String generateReferenceNo() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "SR-" + date + "-" + suffix;
    }
}
