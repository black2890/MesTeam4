package com.mesproject.controller;

import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.entity.WorkOrders;
import com.mesproject.repository.WorkOrdersRepository;
import com.mesproject.service.WorkOrdersService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WorkController {
    private final WorkOrdersService workOrdersService;
    private final WorkOrdersRepository workOrdersRepository;


    @GetMapping("/data/workOrders")
    public String workOrders(Model model){

//        List<WorkOrders> workOrders = workOrdersRepository.findAll();
        List<WorkOrders> workOrders = workOrdersRepository.findByDailyWorkOrders(LocalDate.now());
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
    //다중 시작 처리
    @PostMapping("/works/start")
    public ResponseEntity<?> startWorks(@RequestBody List<Long> rnos){
        Collections.sort(rnos);

        for (Long rno: rnos){
            WorkOrders workOrders = workOrdersRepository.findById(rno)
                    .orElseThrow(EntityNotFoundException::new);
            WorkOrdersDto workOrdersDto = new WorkOrdersDto();
            workOrdersDto.setWorkOrderId(rno);
            workOrdersDto.setStart(workOrders.getScheduledDate());
            workOrdersDto.setWorker("김철수");
            workOrdersDto.setEnd(workOrders.getScheduledDate().plusHours(workOrders.getDuration().toHours()));
            workOrdersService.start(workOrdersDto);
            workOrdersService.end(workOrdersDto);
        }
        return ResponseEntity.ok()
                .build();
    }



}
