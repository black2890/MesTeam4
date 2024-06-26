package com.mesproject.repository;

import com.mesproject.entity.WorkOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkOrdersRepository extends JpaRepository<WorkOrders, Long> {
    List<WorkOrders> findByWorkPlan_WorkPlanId(Long workPlanId);


}
