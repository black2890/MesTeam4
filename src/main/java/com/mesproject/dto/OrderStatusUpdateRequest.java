package com.mesproject.dto;

import com.mesproject.constant.OrdersStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderStatusUpdateRequest {
   private List<Long> orderIds;
   private OrdersStatus status;
}
