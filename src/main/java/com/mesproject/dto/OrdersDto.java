package com.mesproject.dto;

import com.mesproject.constant.OrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Getter @Setter
public class OrdersDto {

    private Long orderId;
    private String productName;
    private Long quantity;
    private String vendorName;
    private String deliveryAddress;
    private LocalDateTime deliveryDate;
    private OrdersStatus ordersStatus;


}
