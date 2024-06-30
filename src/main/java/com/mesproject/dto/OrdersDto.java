package com.mesproject.dto;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.entity.Orders;
import com.mesproject.entity.Product;
import com.mesproject.entity.Vendor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {

    private Long orderId;

    private String productName;

    private String vendorName;

    private Long quantity;
    private LocalDateTime deliveryDate;
    private String deliveryAddress;

    private OrdersStatus ordersStatus;

    private LocalDate regDate;

    public  static OrdersDto createOrdersDto(Orders orders){
        return new OrdersDto(orders.getOrderId(),
                orders.getProduct().getProductName(),
                orders.getVendor().getVendorName(),
                orders.getQuantity(),
                orders.getDeliveryDate(),
                orders.getDeliveryAddress(),
                orders.getOrdersStatus(),
                orders.getRegDate()

        );
    }



}
