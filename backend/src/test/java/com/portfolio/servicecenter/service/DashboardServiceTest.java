package com.portfolio.servicecenter.service;

import com.portfolio.servicecenter.domain.ServiceStatus;
import com.portfolio.servicecenter.repository.ServiceRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    ServiceRequestRepository repository;

    @InjectMocks
    DashboardService service;

    @Test
    void countsReturnsTotalAndEveryWorkflowStatus() {
        when(repository.count()).thenReturn(7L);
        when(repository.countByStatus(ServiceStatus.RECEIVED)).thenReturn(2L);

        var result = service.counts();

        assertThat(result.get("TOTAL")).isEqualTo(7L);
        assertThat(result.get("RECEIVED")).isEqualTo(2L);
        assertThat(result.keySet()).contains("INSPECTING", "REPAIRING", "COMPLETED", "SHIPPED");
    }
}
