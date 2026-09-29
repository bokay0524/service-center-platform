package com.portfolio.servicecenter.service;

import com.portfolio.servicecenter.domain.ServiceRequest;
import com.portfolio.servicecenter.domain.ServiceStatus;
import com.portfolio.servicecenter.dto.ServiceRequestDtos.CreateItemRequest;
import com.portfolio.servicecenter.dto.ServiceRequestDtos.CreateRequest;
import com.portfolio.servicecenter.repository.ServiceRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceTest {

    @Mock
    ServiceRequestRepository repository;

    @InjectMocks
    ServiceRequestService service;

    @Test
    void createBuildsMasterDetailAggregate() {
        when(repository.save(any(ServiceRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(new CreateRequest(
                "Demo", "010-1111-2222", "demo@example.com", "Seoul", "SEOUL-01",
                List.of(new CreateItemRequest("Audio", "MODEL-A", "SN-001", "No power"))
        ));

        assertThat(response.referenceNo()).startsWith("SR-");
        assertThat(response.status()).isEqualTo(ServiceStatus.RECEIVED);
        assertThat(response.items()).hasSize(1);
    }
    @Test
    void updateStatusChangesMasterAndAllItems() {
        ServiceRequest request = new ServiceRequest();
        request.setId(UUID.randomUUID());
        request.setStatus(ServiceStatus.RECEIVED);
        var item = new com.portfolio.servicecenter.domain.ServiceItem();
        item.setProductCategory("Audio");
        item.setModelName("MODEL-A");
        item.setSymptom("No power");
        request.addItem(item);
        when(repository.findById(request.getId())).thenReturn(Optional.of(request));

        var response = service.updateStatus(request.getId(), ServiceStatus.REPAIRING);

        assertThat(response.status()).isEqualTo(ServiceStatus.REPAIRING);
        assertThat(response.items()).allMatch(i -> i.status() == ServiceStatus.REPAIRING);
    }

}
