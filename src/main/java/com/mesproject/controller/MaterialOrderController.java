package com.mesproject.controller;

import com.mesproject.dto.MaterialOrderDto;
import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.entity.Orders;
import com.mesproject.entity.OrdersMaterials;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import com.mesproject.service.MaterialInOutService;
import com.mesproject.service.MaterialOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class MaterialOrderController {

    private final MaterialOrdersRepository materialOrdersRepository;
    private final MaterialInOutRepository materialInOutRepository;
    private final MaterialInOutService materialInOutService;
    private final MaterialOrderService materialOrderService;

    @GetMapping("/data/materialOrders")
    public String materialOrders(Model model){


        List<MaterialOrders> materialOrders = materialOrdersRepository.findAll();
        model.addAttribute("materialOrders", materialOrders);
        return "materialOrders";
    }

    @PostMapping("/material/in")
    public ResponseEntity<?> materialIn(@RequestBody MaterialOrderDto materialOrderDto){
        materialInOutService.In(materialOrderDto);

        return ResponseEntity.ok()
                .build();
    }
    @GetMapping("/data/materialInOut")
    public String materialInOut(Model model){


        List<MaterialInOut> materialInOuts = materialInOutRepository.findAll();
        model.addAttribute("materialInOut", materialInOuts);
        return "materialInOut";
    }
    @PostMapping("/materials/in")
    public ResponseEntity<?> materialsIn(@RequestBody List<Long> rnos){
        Collections.sort(rnos);

        for(Long rno: rnos){

            MaterialOrderDto materialOrderDto = new MaterialOrderDto();
            materialOrderDto.setMaterialOrderId(rno);
            materialOrderDto.setStorageWorker("김철수");
            materialInOutService.In(materialOrderDto);
        }

        return ResponseEntity.ok()
                .build();
    }





}
