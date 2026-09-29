package com.portfolio.servicecenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "service_item")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(nullable = false, length = 80)
    private String productCategory;

    @Column(nullable = false, length = 100)
    private String modelName;

    @Column(length = 100)
    private String serialNo;

    @Column(nullable = false, length = 1000)
    private String symptom;

    @Column(length = 2000)
    private String repairNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ServiceStatus status = ServiceStatus.RECEIVED;
}
