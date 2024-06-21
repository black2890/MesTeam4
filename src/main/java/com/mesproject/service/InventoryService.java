package com.mesproject.service;

import com.mesproject.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Map<String, Integer> getTotalStockByProductNameAndStatus() {
        List<Object[]> results = inventoryRepository.findTotalStockByProductNameAndStatus();
        Map<String, Integer> stockMap = new HashMap<>();
        for (Object[] result : results) {
            String productName = (String) result[0];
            Long totalQuantity = (Long) result[1];
            stockMap.put(productName, totalQuantity.intValue());
        }
        return stockMap;
    }
}
