package com.mesproject.service;

import com.mesproject.entity.WorkPlan;
import com.mesproject.repository.WorkPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkPlanService {

    @Autowired
    private WorkPlanRepository workPlanRepository;

    public List<WorkPlan> getAllWorkPlans() {
        return workPlanRepository.findAllWithProducts();
    }

    public WorkPlan saveWorkPlan(WorkPlan workPlan) {
        return workPlanRepository.save(workPlan);
    }
}

