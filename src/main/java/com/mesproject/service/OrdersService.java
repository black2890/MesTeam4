package com.mesproject.service;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.entity.Orders;
import com.mesproject.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    public void updateOrderStatus(List<Long> orderIds, OrdersStatus status) {
        ordersRepository.updateStatusByIds(orderIds, status);
        if(status ==OrdersStatus.RETRIEVALCOMPLETED){

        }
    }

    public List<Object[]> getProductOrdersSummary() {
        return ordersRepository.findProductOrdersSummary();
    }

    /*
    1. 수주의 수량만큼 재고 차감
    2. 수주-작업계획코드 테이블에서 수주코드로 작업계획코드 찾음 (리스트일 수도 있음)
    3. 재고 테이블에서 작업계획 코드로 재고 조회후, 그 수량 차감
     */
    public void retrievalCompleted(List<Long> orderIds){
        for(Long orderId : orderIds){
            Orders orders = ordersRepository.findById(orderId)
                    .orElseThrow(EntityNotFoundException::new);
            


        }
    }
}
