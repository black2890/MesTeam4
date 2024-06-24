package com.mesproject.repository;

import com.mesproject.entity.WorkOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mesproject.constant.WorkOrdersStatus;
import com.mesproject.dto.OrdersDto;
import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.Orders;
import com.mesproject.entity.WorkOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkOrdersRepository extends JpaRepository<WorkOrders, Long> {

    @Query("SELECT NEW com.mesproject.dto.WorkOrdersDto(w.workOrderId, p.productName, w.workName, w.worker, w.scheduledDate, w.duration, w.start, w.end, w.workOrdersStatus) " +
            "FROM WorkOrders w " +
            "LEFT JOIN w.workPlan wp " +
            "LEFT JOIN wp.product p")
    Page<WorkOrdersDto> findWorkOrders(Pageable pageable);

    @Query("SELECT NEW com.mesproject.dto.WorkOrdersDto(w.workOrderId, p.productName, w.workName, w.worker, w.scheduledDate, w.duration, w.start, w.end, w.workOrdersStatus) " +
            "FROM WorkOrders w " +
            "LEFT JOIN w.workPlan wp " +
            "LEFT JOIN wp.product p " +
            "WHERE (:searchType = '제품명' AND p.productName LIKE %:searchValue%) OR " +
            "      (:searchType = '작업명' AND w.workName LIKE %:searchValue%) OR " +
            "      (:searchType = '작업자' AND w.worker LIKE %:searchValue%)")
    Page<WorkOrdersDto> findWorkOrdersBySearchOption(@Param("searchType") String searchType,
                                   @Param("searchValue") String searchValue,
                                   Pageable pageable);
}
