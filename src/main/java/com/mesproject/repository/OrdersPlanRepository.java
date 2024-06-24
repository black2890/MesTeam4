package com.mesproject.repository;

import com.mesproject.entity.OrdersPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersPlanRepository extends JpaRepository<OrdersPlan, Long> {
    List<OrdersPlan> findByWorkPlan_WorkPlanId(Long workPlanId);
    List<OrdersPlan> findByOrders_OrderId(Long orderId);
    List<OrdersPlan> findByOrders_OrderIdOrderByOrders_OrderIdAsc(Long orderId);
}
