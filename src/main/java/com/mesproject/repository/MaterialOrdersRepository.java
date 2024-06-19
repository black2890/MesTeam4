package com.mesproject.repository;

import com.mesproject.entity.Equipment;
import com.mesproject.entity.MaterialOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialOrdersRepository extends JpaRepository<MaterialOrders, Long> {

    @Query(value = "SELECT * FROM material_orders LIMIT :start, :length", nativeQuery = true)
    List<MaterialOrders> findData(@Param("start") int start, @Param("length") int length);
}

