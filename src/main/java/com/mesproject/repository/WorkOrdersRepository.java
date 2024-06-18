package com.mesproject.repository;

import com.mesproject.entity.WorkOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrdersRepository extends JpaRepository<WorkOrders, Long> {

}
