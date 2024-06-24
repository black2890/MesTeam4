package com.mesproject.repository;

import com.mesproject.entity.MaterialOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mesproject.dto.MaterialOrdersDto;
import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.Equipment;
import com.mesproject.entity.MaterialOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MaterialOrdersRepository extends JpaRepository<MaterialOrders, Long> {
}

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

