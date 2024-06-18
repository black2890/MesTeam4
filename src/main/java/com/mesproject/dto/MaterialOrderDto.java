package com.mesproject.dto;

import com.mesproject.constant.MaterialOrdersStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MaterialOrderDto {
    private Long productId;
    private LocalDateTime materialOrderDate;
    private LocalDateTime deliveryDate;
    private Long quantity;

}
