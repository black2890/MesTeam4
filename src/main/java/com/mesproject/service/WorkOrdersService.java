package com.mesproject.service;

import com.mesproject.dto.DataTableDto;
import com.mesproject.entity.Orders;
import com.mesproject.entity.WorkOrders;
import com.mesproject.repository.WorkOrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOrdersService {

    private final WorkOrdersRepository workOrdersRepository;

    // 페이지 및
    public DataTableDto getWorkOrdersData(@RequestBody MultiValueMap<String,String> formData){
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        int total = (int) workOrdersRepository.count();
        List<WorkOrders> data = workOrdersRepository.findData(start, length);

        DataTableDto dto = new DataTableDto();
        dto.setDraw(draw);
        dto.setRecordsFiltered(total);
        dto.setRecordsTotal(total);
        dto.setData(data);

        return dto;
    }

}