package com.mesproject.entity;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.vendorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @JoinColumn
    private Long productId;
    private Long vendorId;
    private Long workPlanId;
    private Long materialOrderId;

    private Long quantity;
    private LocalDateTime deliveryDate;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;

}
