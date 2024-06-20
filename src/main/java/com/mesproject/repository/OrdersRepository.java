package com.mesproject.repository;

import com.mesproject.dto.OrdersDto;
import com.mesproject.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT NEW com.mesproject.dto.OrdersDto(o.orderId, p.productName, o.quantity, v.vendorName, o.deliveryAddress, o.deliveryDate, o.ordersStatus)" +
            "FROM Orders o JOIN o.product p JOIN o.vendor v ")
    Page<OrdersDto> findOrders(Pageable pageable);

    @Query("SELECT NEW com.mesproject.dto.OrdersDto(o.orderId, p.productName, o.quantity, v.vendorName, o.deliveryAddress, o.deliveryDate, o.ordersStatus) " +
            "FROM Orders o JOIN o.product p JOIN o.vendor v " +
            "WHERE (:searchType = '제품명' AND p.productName LIKE %:searchValue%) OR " +
            "      (:searchType = '고객명(업체)' AND v.vendorName LIKE %:searchValue%)")
    Page<OrdersDto> findOrdersByProductOrVendor(
            @Param("searchType") String searchType,
            @Param("searchValue") String searchValue,
            Pageable pageable);
}
