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

    @Query("SELECT NEW com.mesproject.dto.OrdersDto(o.orderId, p.productName, o.quantity, v.vendorName, o.deliveryAddress, o.deliveryDate, o.ordersStatus) FROM Orders o JOIN o.product p JOIN o.vendor v")
    Page<OrdersDto> findOrders(Pageable pageable);

    @Query("SELECT o FROM Orders o " +
            "JOIN o.product p " +
            "JOIN o.vendor v " +
            "WHERE (:productName IS NULL OR p.productName LIKE %:productName%) AND " +
            "(:vendorName IS NULL OR v.vendorName LIKE %:vendorName%)")
    Page<OrdersDto> findOrdersByProductOrVendor(
            @Param("productName") String productName,
            @Param("vendorName") String vendorName,
            Pageable pageable);
}
