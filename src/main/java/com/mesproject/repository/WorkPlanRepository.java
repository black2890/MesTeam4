package com.mesproject.repository;

import com.mesproject.entity.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface WorkPlanRepository extends JpaRepository<WorkPlan, Long> {
    @Query("SELECT wp FROM WorkPlan wp JOIN FETCH wp.product")
    List<WorkPlan> findAllWithProducts();



    @Query("SELECT wp FROM WorkPlan wp WHERE (wp.product.productId = :productId1 OR wp.product.productId = :productId2) " +
            "AND wp.start >= :startDate ORDER BY wp.start ASC")
    List<WorkPlan> findByProductIdAndStartDateAfter(@Param("productId1") Long productId1,
                                                    @Param("productId2") Long productId2,
                                                    @Param("startDate") LocalDateTime startDate);
}
