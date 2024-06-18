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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private Long quantity;
    private LocalDateTime deliveryDate;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;



//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "work_plan_id")
//    private WorkPlan workPlan;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "material_order_id")
//    private MaterialOrders materialOrder;

}
