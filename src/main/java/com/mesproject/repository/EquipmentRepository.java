package com.mesproject.repository;

import com.mesproject.entity.Equipment;
import com.mesproject.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query(value = "SELECT * FROM equipment LIMIT :start, :length", nativeQuery = true)
    List<Equipment> findData(@Param("start") int start, @Param("length") int length);
}
