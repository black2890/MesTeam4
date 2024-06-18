package com.mesproject.dto;

import com.mesproject.entity.Orders;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataTableDto {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<Orders> data;

    public List<Orders> getData() {

        if (CollectionUtils.isEmpty(data)) {
            data = new ArrayList();
        }

        return data;
    }
}
