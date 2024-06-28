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
import java.util.Map;
import java.util.Optional;


public interface WorkPlanRepository extends JpaRepository<WorkPlan, Long> {
    @Query("SELECT wp FROM WorkPlan wp JOIN FETCH wp.product")
    List<WorkPlan> findAllWithProduct();



    @Query("SELECT wp FROM WorkPlan wp WHERE (wp.product.productId = :productId1 OR wp.product.productId = :productId2) " +
            "AND wp.start >= :startDate ORDER BY wp.start ASC")
    List<WorkPlan> findByProductIdAndStartDateAfter(@Param("productId1") Long productId1,
                                                    @Param("productId2") Long productId2,
                                                    @Param("startDate") LocalDateTime startDate);

    List<WorkPlan> findByProduct_ProductIdAndEndAfter(Long productId, LocalDateTime endDate);

    Optional<WorkPlan> findFirstByProduct_ProductIdAndStartGreaterThanEqualOrderByStartDescWorkPlanIdDesc(Long productId, LocalDateTime startDate);

    @Query("SELECT w FROM WorkPlan w WHERE w.product.productId = :productId AND FUNCTION('DATE', w.end) = :endDate")
    List<WorkPlan> findByProduct_ProductIdAndEnd(@Param("productId") Long productId, @Param("endDate") LocalDate endDate);

    @Query("SELECT w FROM WorkPlan w WHERE (w.product.productId = :productId1 OR w.product.productId = :productId2) AND FUNCTION('DATE', w.end) = :endDate")
    List<WorkPlan> findByProducts_ProductIdAndEnd(@Param("productId1") Long productId1,
                                                  @Param("productId2") Long productId2,
                                                  @Param("endDate") LocalDate endDate);


    @Query("SELECT NEW com.mesproject.dto.ProductionDataDto(p.productName, SUM(wp.quantity), wp.end) " +
            "FROM WorkPlan wp " +
            "JOIN wp.product p " +
            "GROUP BY p.productName, wp.end " +
            "ORDER BY wp.end")
    List<ProductionDataDto> aggregateProductionDataByProduct();

//    @Query(value = "SELECT DISTINCT o.order_id AS orderId " +
//            "FROM work_plan wp " +
//            "JOIN work_orders wo ON wp.work_plan_id = wo.work_plan_id " +
//            "JOIN material_in_out mi ON wo.work_order_id = mi.work_order_id " +
//            "JOIN material_orders mo ON mi.material_order_id = mo.material_order_id " +
//            "JOIN orders_materials om ON mo.material_order_id = om.material_orders_id " +
//            "JOIN orders o ON om.orders_id = o.order_id " +
//            "WHERE wp.work_plan_id = :workPlanId", nativeQuery = true)
//    List<Map<String, Object>> findOrdersByWorkPlanId(@Param("workPlanId") Long workPlanId);

    @Query("SELECT DISTINCT o.orderId AS orderId, " +
            "v.vendorName AS vendorName, " +
            "p.productName AS productName, " +
            "o.quantity AS quantity, " +
            "o.deliveryAddress AS deliveryAddress " +
            "FROM WorkPlan wp " +
            "JOIN WorkOrders wo ON wp.workPlanId = wo.workPlan.workPlanId " +
            "JOIN MaterialInOut mi ON wo.workOrderId = mi.workOrders.workOrderId " +
            "JOIN MaterialOrders mo ON mi.materialOrders.materialOrderId = mo.materialOrderId " +
            "JOIN OrdersMaterials om ON mo.materialOrderId = om.materialOrders.materialOrderId " +
            "JOIN Orders o ON om.orders.orderId = o.orderId " +
            "JOIN Product p ON o.product.productId = p.productId " +
            "JOIN Vendor v ON o.vendor.vendorId = v.vendorId " +
            "WHERE wp.workPlanId = :workPlanId")
    List<Map<String, Object>> findOrdersByWorkPlanId(@Param("workPlanId") Long workPlanId);
}
