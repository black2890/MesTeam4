package com.mesproject.dto;

import com.mesproject.constant.MaterialOrdersStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class MaterialOrderDto {
    private Long materialOrderId;
    private Long productId;
    private LocalDateTime materialOrderDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime storageDate;
    private Long quantity;
    private String storageWorker;

}
