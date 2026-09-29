package com.portfolio.servicecenter.schedule;

import com.portfolio.servicecenter.domain.ServiceRequest;
import com.portfolio.servicecenter.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PrivacyMaskingScheduler {
    private final ServiceRequestRepository repository;
    private final int retentionYears;

    public PrivacyMaskingScheduler(ServiceRequestRepository repository,
                                   @Value("${app.privacy.retention-years}") int retentionYears) {
        this.repository = repository;
        this.retentionYears = retentionYears;
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void maskExpiredPersonalData() {
        LocalDateTime cutoff = LocalDateTime.now().minusYears(retentionYears);
        List<ServiceRequest> targets = repository.findByCreatedAtBeforeAndPrivacyMaskedFalse(cutoff);

        targets.forEach(request -> {
            request.setCustomerPhone(maskPhone(request.getCustomerPhone()));
            request.setCustomerEmail(maskEmail(request.getCustomerEmail()));
            request.setCustomerAddress("MASKED");
            request.setPrivacyMasked(true);
        });
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return "****";
        return "***-****-" + phone.substring(phone.length() - 4);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        return "***@" + email.substring(email.indexOf('@') + 1);
    }
}
