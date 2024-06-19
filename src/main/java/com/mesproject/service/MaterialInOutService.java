package com.mesproject.service;

import com.mesproject.dto.DataTableDto;
import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialInOutService {

    private final MaterialInOutRepository materialInOutRepository;

    // 페이지 및
    public DataTableDto getMaterialInOutData(@RequestBody MultiValueMap<String,String> formData){
        int draw = Integer.parseInt(formData.get("draw").get(0));
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        int total = (int) materialInOutRepository.count();
        List<MaterialInOut> data = materialInOutRepository.findData(start, length);

        DataTableDto dto = new DataTableDto();
        dto.setDraw(draw);
        dto.setRecordsFiltered(total);
        dto.setRecordsTotal(total);
        dto.setData(data);

        return dto;
    }

}