package com.mesproject.repository;

import com.mesproject.entity.Orders;
import com.mesproject.entity.WorkOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkOrdersRepository extends JpaRepository<WorkOrders, Long> {

    @Query("SELECT o FROM WorkOrders o ORDER BY o.id ASC")
    List<WorkOrders> findData(@Param("start") int start, @Param("length") int length);
}