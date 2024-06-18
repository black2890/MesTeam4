package com.mesproject.entity;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.vendorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @OneToMany
    @JoinColumn(name = "productId")
    private List<Product> productId;
    private Long vendorId;
    private Long workPlanId;
    private Long materialOrderId;

    private Long quantity;
    private LocalDateTime deliveryDate;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;

}
