package com.portfolio.servicecenter.controller;

import com.portfolio.servicecenter.dto.ServiceRequestDtos.CreateRequest;
import com.portfolio.servicecenter.dto.ServiceRequestDtos.Response;
import com.portfolio.servicecenter.dto.ServiceRequestDtos.UpdateStatusRequest;
import com.portfolio.servicecenter.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
public class ServiceRequestController {
    private final ServiceRequestService service;

    public ServiceRequestController(ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<Response> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Response findOne(@PathVariable UUID id) {
        return service.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Response create(@Valid @RequestBody CreateRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/status")
    public Response updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateStatusRequest request) {
        return service.updateStatus(id, request.status());
    }
}
