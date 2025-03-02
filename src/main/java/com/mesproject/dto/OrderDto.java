package com.mesproject.dto;

import com.mesproject.constant.OrdersStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
public class OrderDto {

    private Long productId;
    private Long vendorId;

    private Long quantity;
    private LocalDateTime deliveryDate;
    private String deliveryAddress;

}
