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

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
    private Long workPlanId;
    private Long materialOrderId;

    private Long quantity;
    private LocalDateTime deliveryDate;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;

}
