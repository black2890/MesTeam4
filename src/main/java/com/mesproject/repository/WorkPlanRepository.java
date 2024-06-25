package com.mesproject.repository;

import com.mesproject.dto.ProductionDataDto;
import com.mesproject.entity.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface WorkPlanRepository extends JpaRepository<WorkPlan, Long> {
    @Query("SELECT wp FROM WorkPlan wp JOIN FETCH wp.product")
    List<WorkPlan> findAllWithProduct();

    @Query("SELECT NEW com.mesproject.dto.ProductionDataDto(p.productName, SUM(wp.quantity), wp.end) " +
            "FROM WorkPlan wp " +
            "JOIN wp.product p " +
            "GROUP BY p.productName, wp.end " +
            "ORDER BY wp.end")
    List<ProductionDataDto> aggregateProductionDataByProduct();


}
