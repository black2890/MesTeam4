package com.mesproject.service;

import com.mesproject.constant.InventoryStatus;
import com.mesproject.dto.InventoryInDto;
import com.mesproject.entity.Inventory;
import com.mesproject.repository.InventoryRepository;
import com.mesproject.repository.MaterialInOutRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MaterialInOutRepository materialInOutRepository;

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

    public Map<String, Integer> getMaterialTotalStockByProductNameAndStatus() {
        List<Object[]> results = materialInOutRepository.findMaterialTotalStockByProductNameAndStatus();
        Map<String, Integer> stockMap = new HashMap<>();
        for (Object[] result : results) {
            String productName = (String) result[0];
            Long totalQuantity = (Long) result[1];
            stockMap.put(productName, totalQuantity.intValue());
        }
        return stockMap;
    }

    public void in(InventoryInDto inventoryInDto) {
       Long inventoryId = inventoryInDto.getInventoryId();
        String worker = inventoryInDto.getStorageWorker();
        LocalDateTime storageDate = inventoryInDto.getStorageDate();


            Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(EntityNotFoundException::new);
            inventory.setInventoryStatus(InventoryStatus.STORAGECOMPLETED);
            inventory.setStorageDate(storageDate);
            inventory.setStorageWorker(worker);

    }

}
