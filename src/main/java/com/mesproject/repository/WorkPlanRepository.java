package com.mesproject.repository;

import com.mesproject.dto.ProductionDataDto;
import com.mesproject.entity.WorkPlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface WorkPlanRepository extends JpaRepository<WorkPlan, Long> {
    @Query("SELECT wp FROM WorkPlan wp JOIN FETCH wp.product")
    List<WorkPlan> findAllWithProducts();



    @Query("SELECT wp FROM WorkPlan wp WHERE (wp.product.productId = :productId1 OR wp.product.productId = :productId2) " +
            "AND wp.start >= :startDate ORDER BY wp.start ASC")
    List<WorkPlan> findByProductIdAndStartDateAfter(@Param("productId1") Long productId1,
                                                    @Param("productId2") Long productId2,
                                                    @Param("startDate") LocalDateTime startDate);

    List<WorkPlan> findByProduct_ProductIdAndEndAfter(Long productId, LocalDateTime endDate);

    Optional<WorkPlan> findFirstByProduct_ProductIdAndStartGreaterThanEqualOrderByStartDescWorkPlanIdDesc(Long productId, LocalDateTime startDate);

    @Query("SELECT w FROM WorkPlan w WHERE w.product.productId = :productId AND FUNCTION('DATE', w.end) = :endDate")
    List<WorkPlan> findByProduct_ProductIdAndEnd(@Param("productId") Long productId, @Param("endDate") LocalDate endDate);


    List<WorkPlan> findAllWithProduct();

    @Query("SELECT NEW com.mesproject.dto.ProductionDataDto(p.productName, SUM(wp.quantity), wp.end) " +
            "FROM WorkPlan wp " +
            "JOIN wp.product p " +
            "GROUP BY p.productName, wp.end " +
            "ORDER BY wp.end")
    List<ProductionDataDto> aggregateProductionDataByProduct();


}
