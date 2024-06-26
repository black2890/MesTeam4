package com.mesproject.entity;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.vendorType;
import com.mesproject.dto.OrderDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    private Long workPlanId;
    private Long materialOrderId;

    private Long quantity;
    private LocalDateTime deliveryDate;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;

    @OneToMany(mappedBy = "orders",
            cascade = CascadeType.ALL
            ,orphanRemoval = true,
            fetch=FetchType.LAZY)
    private List<OrdersMaterials> ordersMaterialsList = new ArrayList<>();


    @OneToMany(mappedBy = "orders",
            cascade = CascadeType.ALL
            ,orphanRemoval = true,
            fetch=FetchType.LAZY)
    private List<OrdersPlan> ordersPlanList = new ArrayList<>();

    public void addOrdersMaterials(OrdersMaterials ordersMaterials){

        ordersMaterialsList.add(ordersMaterials);

    }

    public void addOrdersPlan(OrdersPlan ordersPlan){

        ordersPlanList.add(ordersPlan);

    }

    public static Orders createOrder(List<OrdersMaterials> ordersMaterialsList,
                                     List<OrdersPlan> ordersPlanList,
                                     Product product,
                                     Vendor vendor,
                                     OrderDto orderDto){

        Orders order = new Orders();

        for(OrdersMaterials ordersMaterials : ordersMaterialsList){
            order.addOrdersMaterials(ordersMaterials);
            ordersMaterials.setOrders(order);
        }

        for(OrdersPlan ordersPlan : ordersPlanList){
            order.addOrdersPlan(ordersPlan);
            ordersPlan.setOrders(order);
        }

        order.setProduct(product);
        order.setVendor(vendor);
        order.setQuantity(orderDto.getQuantity());
        if(orderDto.getQuantity() ==999){
            order.setQuantity(1000L);
        }
        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        order.setOrdersStatus(OrdersStatus.PENDINGSTORAGE);

        return order;
    }

}
