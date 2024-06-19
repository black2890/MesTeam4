package com.mesproject.controller;

import com.mesproject.entity.Inventory;
import com.mesproject.entity.Product;
import com.mesproject.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping("/inventoryData")
    public Map<String, Object> getInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("data", inventories);
        return response;
    }
}
