package com.mesproject.repository;

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
//            "WHERE o.ordersStatus = 'PENDINGSTORAGE' " +
            "GROUP BY o.product.productName, o.vendor.vendorName")
    List<Object[]> findProductOrdersSummary();
}


