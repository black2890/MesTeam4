package com.mesproject.repository;

import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.entity.MaterialOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialOrdersRepository extends JpaRepository<MaterialOrders, Long> {

    @Query("SELECT m FROM MaterialOrders m WHERE m.product.productName LIKE %:productName% ")
    List<MaterialOrders> findByNameContaining(@Param("productName") String keyword);


    @Query("SELECT m FROM MaterialOrders m WHERE m.materialOrderDate >= :start")
    List<MaterialOrders> findByMaterialOrderDate(@Param("start") LocalDateTime start);

    @Query("SELECT m FROM MaterialOrders m WHERE m.materialOrderDate BETWEEN :start AND :end")
    List<MaterialOrders> findByMaterialOrderDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT m FROM MaterialOrders m WHERE m.deliveryDate >= :start")
    List<MaterialOrders> findByMaterialDeliveryDate(@Param("start") LocalDateTime start);

    @Query("SELECT m FROM MaterialOrders m WHERE m.deliveryDate BETWEEN :start AND :end")
    List<MaterialOrders> findByMaterialDeliveryDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT m FROM MaterialOrders m WHERE m.materialOrdersStatus = :materialOrdersStatus")
    List<MaterialOrders> findByMaterialConditionContaining(@Param("materialOrdersStatus") MaterialOrdersStatus materialOrdersStatus);
}
