package com.portfolio.servicecenter.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "service_request")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reference_no", nullable = false, unique = true, length = 32)
    private String referenceNo;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 30)
    private String customerPhone;

    @Column(length = 150)
    private String customerEmail;

    @Column(length = 300)
    private String customerAddress;

    @Column(nullable = false, length = 30)
    private String branchCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ServiceStatus status = ServiceStatus.RECEIVED;

    @Column(nullable = false)
    private boolean privacyMasked = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceItem> items = new ArrayList<>();

    public void addItem(ServiceItem item) {
        items.add(item);
        item.setServiceRequest(this);
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
