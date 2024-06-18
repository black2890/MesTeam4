package com.mesproject.repository;

import com.mesproject.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o ORDER BY o.id ASC")
    List<Orders> findData(@Param("start") int start, @Param("length") int length);
}
