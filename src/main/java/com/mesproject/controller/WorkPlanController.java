package com.mesproject.controller;
import com.mesproject.entity.WorkPlan;
import com.mesproject.service.WorkPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WorkPlanController {

    private static final Logger logger = LoggerFactory.getLogger(WorkPlanController.class);

    @Autowired
    private WorkPlanService workPlanService;

    @GetMapping("/api/workplans")
    public List<WorkPlan> getAllWorkPlans() {
        List<WorkPlan> workPlans = workPlanService.getAllWorkPlans();
        for (WorkPlan workPlan : workPlans) {
            logger.info("WorkPlan: {}", workPlan);
        }
        return workPlans;
    }
}
