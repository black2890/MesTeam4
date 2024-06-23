package com.mesproject.repository;

import com.mesproject.entity.OrdersMaterials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersMaterialsRepository extends JpaRepository<OrdersMaterials, Long> {
    List<OrdersMaterials> findByOrders_OrderId(Long orderId);
    List<OrdersMaterials> findByMaterialOrders_MaterialOrderId(Long materialOrderId);
}
