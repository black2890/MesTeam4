package com.mesproject.repository;

import com.mesproject.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProcessRepository extends JpaRepository<Process, Long> {

    @Query("SELECT DISTINCT p.processType FROM Process p ORDER BY p.processType ASC")
    List<String> findDistinctProcessTypesOrdered();
}
