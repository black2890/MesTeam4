package com.mesproject.repository;

import com.mesproject.entity.OrdersPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersPlanRepository extends JpaRepository<OrdersPlan, Long> {
    List<OrdersPlan> findByWorkPlan_WorkPlanId(Long workPlanId);
    Optional<OrdersPlan> findFirstByWorkPlan_WorkPlanIdOrderByOrders_OrderIdAsc(Long workPlanId);
    List<OrdersPlan> findByOrders_OrderId(Long orderId);
    List<OrdersPlan> findByOrders_OrderIdOrderByOrders_OrderIdAsc(Long orderId);

}
