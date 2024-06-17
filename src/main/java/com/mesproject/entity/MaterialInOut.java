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
    //private Long workOrderId;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private MaterialInOutStatus materialInOutStatus;

    private LocalDateTime storageDate;
    private LocalDateTime retrievalDate;
    private String storageWorker;
    private String retrievalWorker;
}
