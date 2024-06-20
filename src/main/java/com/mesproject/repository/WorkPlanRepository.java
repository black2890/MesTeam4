package com.mesproject.repository;

import com.mesproject.entity.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkPlanRepository extends JpaRepository<WorkPlan, Long> {

}
