package com.mesproject.repository;

import com.mesproject.dto.MaterialOrdersDto;
import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.Equipment;
import com.mesproject.constant.MaterialOrdersStatus;
import com.mesproject.entity.MaterialOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialOrdersRepository extends JpaRepository<MaterialOrders, Long> {

    @Query("SELECT NEW com.mesproject.dto.MaterialOrdersDto(mo.materialOrderId, mo.materialOrderDate, p.productName, mo.quantity, mo.deliveryDate, mo.materialOrdersStatus) " +
            "FROM MaterialOrders mo " +
            "LEFT JOIN mo.product p ")
    Page<MaterialOrdersDto> findMaterialOrders(Pageable pageable);

    @Query("SELECT NEW com.mesproject.dto.MaterialOrdersDto(mo.materialOrderId, mo.materialOrderDate, p.productName, mo.quantity, mo.deliveryDate, mo.materialOrdersStatus) " +
            "FROM MaterialOrders mo " +
            "LEFT JOIN mo.product p " +
            "WHERE (:searchType = '제품명' AND p.productName LIKE %:searchValue%)")
    Page<MaterialOrdersDto> findMaterialOrdersBySearchOption(@Param("searchType") String searchType,
                                                             @Param("searchValue") String searchValue,
                                                             Pageable pageable);
}
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
