package com.portfolio.servicecenter.dto;

import com.portfolio.servicecenter.domain.ServiceItem;
import com.portfolio.servicecenter.domain.ServiceRequest;
import com.portfolio.servicecenter.domain.ServiceStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ServiceRequestDtos {
    private ServiceRequestDtos() {}

    public record CreateItemRequest(
            @NotBlank String productCategory,
            @NotBlank String modelName,
            String serialNo,
            @NotBlank String symptom
    ) {}

    public record CreateRequest(
            @NotBlank String customerName,
            @NotBlank String customerPhone,
            @Email String customerEmail,
            @Size(max = 300) String customerAddress,
            @NotBlank String branchCode,
            @NotEmpty List<@Valid CreateItemRequest> items
    ) {}

    public record UpdateStatusRequest(@NotNull ServiceStatus status) {}

    public record ItemResponse(
            UUID id,
            String productCategory,
            String modelName,
            String serialNo,
            String symptom,
            String repairNote,
            ServiceStatus status
    ) {
        public static ItemResponse from(ServiceItem item) {
            return new ItemResponse(
                    item.getId(), item.getProductCategory(), item.getModelName(),
                    item.getSerialNo(), item.getSymptom(), item.getRepairNote(), item.getStatus()
            );
        }
    }

    public record Response(
            UUID id,
            String referenceNo,
            String customerName,
            String customerPhone,
            @Email String customerEmail,
            @Size(max = 300) String customerAddress,
            String branchCode,
            ServiceStatus status,
            boolean privacyMasked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<ItemResponse> items
    ) {
        public static Response from(ServiceRequest request) {
            return new Response(
                    request.getId(), request.getReferenceNo(), request.getCustomerName(),
                    request.getCustomerPhone(), request.getCustomerEmail(), request.getCustomerAddress(),
                    request.getBranchCode(), request.getStatus(), request.isPrivacyMasked(),
                    request.getCreatedAt(), request.getUpdatedAt(),
                    request.getItems().stream().map(ItemResponse::from).toList()
            );
        }
    }
}
