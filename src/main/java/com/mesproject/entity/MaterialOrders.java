package com.mesproject.entity;

import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.constant.vendorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MaterialOrders {

    @Id
    @GeneratedValue
    private Long materialOrderId;
    private Long productId;
    private LocalDateTime materialOrderDate;
    private LocalDateTime deliveryDate;
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private MaterialOrdersStatus materialOrdersStatus;
}
