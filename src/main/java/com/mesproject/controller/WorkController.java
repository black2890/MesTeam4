package com.mesproject.controller;

import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.entity.WorkOrders;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.service.WorkOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WorkController {
    private final WorkOrdersService workOrdersService;
    private final WorkOrdersRepository workOrdersRepository;

    @GetMapping("/data/workOrders")
    public String workOrders(Model model){

        List<WorkOrders> workOrders = workOrdersRepository.findAll();
        model.addAttribute("workOrders", workOrders);
        return "workOrders";
    }


    @PostMapping("/work/start")
    public ResponseEntity<?> workStart(@RequestBody WorkOrdersDto workOrdersDto){
        workOrdersService.start(workOrdersDto);

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/work/end")
    public ResponseEntity<?> workEnd(@RequestBody WorkOrdersDto workOrdersDto){
        workOrdersService.end(workOrdersDto);

        return ResponseEntity.ok()
                .build();
    }

}
