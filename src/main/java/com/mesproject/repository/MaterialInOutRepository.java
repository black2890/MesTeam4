package com.mesproject.repository;

import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialInOutRepository extends JpaRepository<MaterialInOut, Long> {

    @Query(value = "SELECT * FROM material_in_out LIMIT :start, :length", nativeQuery = true)
    List<MaterialInOut> findData(@Param("start") int start, @Param("length") int length);
}

