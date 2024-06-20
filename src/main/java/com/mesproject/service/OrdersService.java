package com.mesproject.service;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository orderRepository;

    public void updateOrderStatus(List<Long> orderIds, OrdersStatus status) {
        orderRepository.updateStatusByIds(orderIds, status);
    }
}
