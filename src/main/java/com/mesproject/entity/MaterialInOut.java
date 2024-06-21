package com.mesproject.entity;

import com.mesproject.constant.MaterialInOutStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MaterialInOut {
    @Id
    @GeneratedValue
    private Long inoutId;

    //private Long materialOrderId;
    @OneToOne
    @JoinColumn(name = "work_orders_id")
    private WorkOrders workOrders;

    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private MaterialInOutStatus materialInOutStatus;

    private LocalDateTime storageDate;
    private LocalDateTime retrievalDate;
    private String storageWorker;
    private String retrievalWorker;
}
