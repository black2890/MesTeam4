package com.mesproject.repository;

import com.mesproject.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

    @Query("SELECT DISTINCT p.processType FROM Process p ORDER BY p.processType ASC")
    List<String> findDistinctProcessTypesOrdered();
}
