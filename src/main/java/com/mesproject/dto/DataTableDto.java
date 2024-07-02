package com.mesproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataTableDto {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<?> data;

//    public List<Orders> getData() {
//
//        if (CollectionUtils.isEmpty(data)) {
//            data = new ArrayList();
//        }
//
//        return data;
//    }
}
