package com.mesproject.repository;

import com.mesproject.entity.WorkOrders;
import com.mesproject.entity.WorkPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkOrdersRepository extends JpaRepository<WorkOrders, Long> {
    List<WorkOrders> findByWorkPlan_WorkPlanId(Long workPlanId);

    @Query("SELECT w FROM WorkOrders w WHERE FUNCTION('DATE', w.scheduledDate) = :searchDate")
    List<WorkOrders> findByDailyWorkOrders( @Param("searchDate") LocalDate searchDate);


}
