package com.mesproject.repository;

import com.mesproject.entity.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface WorkPlanRepository extends JpaRepository<WorkPlan, Long> {
    @Query("SELECT wp FROM WorkPlan wp JOIN FETCH wp.product")
    List<WorkPlan> findAllWithProduct();



    List<WorkPlan> findByEndBetweenOrderByEndAsc(LocalDateTime startDate, LocalDateTime endDate);
}
