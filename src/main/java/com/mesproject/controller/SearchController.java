package com.mesproject.controller;

import com.mesproject.entity.MaterialOrders;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import com.mesproject.service.MaterialInOutService;
import com.mesproject.service.MaterialOrderService;
import com.mesproject.service.WorkOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final MaterialOrdersRepository materialOrdersRepository;
    private final MaterialInOutRepository materialInOutRepository;
    private final MaterialInOutService materialInOutService;
    private final MaterialOrderService materialOrderService;
    private final WorkOrdersService workOrdersService;


    //검색
    @GetMapping("/material/search")
    public List<MaterialOrders> searchMaterials(@RequestParam("condition") String condition,
                                              @RequestParam("keyword") String keyword,
                                              @RequestParam("start") String start,
                                              @RequestParam("end") String end,
                                              @RequestParam(value = "deleteCondition", required = false, defaultValue = "defaultCondition") String deleteCondition) {

        if(condition.equals("deletetime")||condition.equals("recovertime")){
            return materialOrderService.searchDateReplies(condition, start, end);
        }else if (condition.equals("replycondition")) {
            return materialOrderService.searchRadioReplyBoards(deleteCondition);
        }
        else {
            return materialOrderService.searchStringReplies(condition, keyword);
        }

    }
    //검색
    @GetMapping("/workOrder/search")
    public List<MaterialOrders> searchWorkOrders(@RequestParam("condition") String condition,
                                              @RequestParam("keyword") String keyword,
                                              @RequestParam("start") String start,
                                              @RequestParam("end") String end,
                                              @RequestParam(value = "deleteCondition", required = false, defaultValue = "defaultCondition") String deleteCondition) {


            return materialOrderService.searchDateReplies(condition, start, end);


    }

}
