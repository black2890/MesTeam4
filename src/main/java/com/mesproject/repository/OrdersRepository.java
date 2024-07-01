package com.mesproject.repository;

import com.mesproject.dto.OrdersDto;
import com.mesproject.constant.OrdersStatus;
import com.mesproject.entity.Orders;
import com.mesproject.entity.WorkPlan;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findAllByProduct_ProductNameContainingOrVendor_VendorNameContaining(String searchValue, String searchValue1, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Orders o SET o.ordersStatus = :status WHERE o.orderId IN (:orderIds)")
    void updateStatusByIds(@Param("orderIds") List<Long> orderIds, @Param("status") Enum status);

    @Query("SELECT o.product.productName, o.vendor.vendorName, SUM(o.quantity) " +
            "FROM Orders o " +
            "WHERE o.ordersStatus = 'DELIVERED' " +
            "GROUP BY o.product.productName, o.vendor.vendorName")
    List<Object[]> findProductOrdersSummary();

     @Query("SELECT NEW com.mesproject.dto.OrdersDto(o.orderId, p.productName, o.quantity, v.vendorName, o.deliveryAddress, o.deliveryDate, o.ordersStatus)" +
            "FROM Orders o JOIN o.product p JOIN o.vendor v ")
    Page<OrdersDto> findOrders(Pageable pageable);

    @Query("SELECT NEW com.mesproject.dto.OrdersDto(o.orderId, p.productName, o.quantity, v.vendorName, o.deliveryAddress, o.deliveryDate, o.ordersStatus) " +
            "FROM Orders o LEFT JOIN o.product p LEFT JOIN o.vendor v " +
            "WHERE (:searchType = '제품명' AND p.productName LIKE %:searchValue%) OR " +
            "      (:searchType = '고객명(업체)' AND v.vendorName LIKE %:searchValue%)")
    Page<OrdersDto> findOrdersByProductOrVendor(
            @Param("searchType") String searchType,
            @Param("searchValue") String searchValue,
            Pageable pageable);
    List<Orders> findByOrdersStatusIn(List<OrdersStatus> statuses);

    @Query("SELECT " +
            "o.orderId AS orderId, " +
            "mo.materialOrderId AS materialOrderId, " +
            "mi.inoutId AS inoutId, " +
            "wo.workOrderId AS workOrderId, " +
            "wp.workPlanId AS workPlanId, " +
            "i.inventoryId AS inventoryId " +
            "FROM Orders o " +
            "JOIN OrdersMaterials om ON o.orderId = om.orders.orderId " +
            "JOIN MaterialOrders mo ON om.materialOrders.materialOrderId = mo.materialOrderId " +
            "JOIN MaterialInOut mi ON mo.materialOrderId = mi.materialOrders.materialOrderId " +
            "JOIN WorkOrders wo ON mi.workOrders.workOrderId = wo.workOrderId " +
            "JOIN WorkPlan wp ON wo.workPlan.workPlanId = wp.workPlanId " +
            "JOIN Inventory i ON wp.workPlanId = i.workPlan.workPlanId " +
            "WHERE o.orderId = :orderId")
    List<Map<String, Object>> findRelatedDataByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Orders o " +
            "JOIN FETCH o.vendor v " +
            "JOIN FETCH o.product p " +
            "WHERE o.orderId = :orderId")
    Orders findByOrderIdWithDetails(@Param("orderId") Long orderId);

    List<Orders> findByVendor_VendorId(Long vendorId);
    Optional<Orders> findFirstByVendor_VendorIdOrderByRegDateDesc(Long vendorId);

}


