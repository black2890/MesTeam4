package com.mesproject.dto;

import com.mesproject.constant.OrdersStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter @Setter
public class OrdersDto {

    private Long orderId;
    private String productName;
    private int quantity;
    private String vendorName;
    private String deliveryAddress;
    private LocalDateTime deliveryDate;
    private OrdersStatus ordersStatus;

    private List data;

    public List getData(){
        if(CollectionUtils.isEmpty(data)){
            data = new ArrayList();
        }
        return data;
    }

}
