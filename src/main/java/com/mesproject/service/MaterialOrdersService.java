package com.mesproject.service;

import com.mesproject.dto.DataTableDto;
import com.mesproject.entity.Equipment;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.repository.EquipmentRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialOrdersService {

    private final MaterialOrdersRepository materialOrdersRepository;

    // 페이지 및
    public DataTableDto getMaterialOrdersData(@RequestBody MultiValueMap<String,String> formData){
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        int total = (int) materialOrdersRepository.count();
        List<MaterialOrders> data = materialOrdersRepository.findData(start, length);

        DataTableDto dto = new DataTableDto();
        dto.setDraw(draw);
        dto.setRecordsFiltered(total);
        dto.setRecordsTotal(total);
        dto.setData(data);

        return dto;
    }

}