package com.mesproject.repository;

import com.mesproject.dto.OrdersDto;
import com.mesproject.constant.OrdersStatus;
import com.mesproject.entity.Orders;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

}


