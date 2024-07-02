package com.mesproject.repository;

import com.mesproject.entity.WorkOrders;
import com.mesproject.entity.WorkPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkOrdersRepository extends JpaRepository<WorkOrders, Long> {
    List<WorkOrders> findByWorkPlan_WorkPlanId(Long workPlanId);

    @Query("SELECT w FROM WorkOrders w WHERE FUNCTION('DATE', w.scheduledDate) = :searchDate")
    List<WorkOrders> findByDailyWorkOrders( @Param("searchDate") LocalDate searchDate);

    // 공정명과 검색 일자 기준으로 가동 시간을 종합하여 반환
    @Query("SELECT SUM(FUNCTION('TIME_TO_SEC', wo.duration)) / 60 " +
            "FROM WorkOrders wo " +
            "WHERE wo.workName = :workName " +
            "AND FUNCTION('DATE_FORMAT', wo.end, '%Y-%m-%d') = :searchDate")
    Long findTotalDurationByWorkNameAndEnd(@Param("workName") String workName, @Param("searchDate") LocalDateTime searchDate);

    // 공정명과 검색 일자 기준으로 제품별 quantity 생산 계획량 List를 반환
    @Query("SELECT SUM(wo.workPlan.quantity) " +
            "FROM WorkOrders wo " +
            "WHERE wo.processType = :processName " +
            "AND FUNCTION('DATE_FORMAT', wo.end, '%Y-%m-%d') = :searchDate " +
            "GROUP BY wo.workPlan.product.productName " +
            "ORDER BY wo.workPlan.product.productName ")
    List<Long> findQuantityByProcessNameAndSearchDate(@Param("processName") String processName,
                                                      @Param("searchDate") LocalDateTime searchDate);

    // 공정명과 검색 일자 기준으로 제품별 quantity 실제 생산량 List를 반환
    @Query("SELECT SUM(iv.quantity) " +
            "FROM WorkOrders wo " +
            "JOIN Inventory iv ON wo.workPlan.workPlanId = iv.workPlan.workPlanId " +
            "WHERE wo.processType = :processName " +
            "AND FUNCTION('DATE_FORMAT', wo.end, '%Y-%m-%d') = :searchDate " +
            "AND iv.inventoryStatus = 'STORAGECOMPLETED' " +
            "GROUP BY wo.workPlan.product.productName " +
            "ORDER BY wo.workPlan.product.productName ")
    List<Long> findQuantityByInventoryStatusAndOrderPlanEnd(@Param("processName") String processName,
                                                            @Param("searchDate") LocalDateTime searchDate);

    // -------------- 메인 화면 메소드 ----------------
    // 작업 지시 상태가 '진행중'인 작업 지시에 해당하는 작업 계획 ID 리스트 반환
    @Query("SELECT wo.workPlan.workPlanId FROM WorkOrders wo WHERE wo.workOrdersStatus = 'INPROGRESS'")
    List<Long> findWorkPlanIdsByWorkOrdersStatusInProgress();


}
