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

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="material_order_id")
    private MaterialOrders materialOrders;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="orders_plan_id")
    private OrdersPlan ordersPlan;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private MaterialInOutStatus materialInOutStatus;

    private LocalDateTime storageDate;
    private LocalDateTime retrievalDate;
    private String storageWorker;
    private String retrievalWorker;
}
