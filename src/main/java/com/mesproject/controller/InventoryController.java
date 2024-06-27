package com.mesproject.controller;

import com.mesproject.entity.Inventory;
import com.mesproject.repository.InventoryRepository;
import com.mesproject.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

//    @PostMapping("/inventoryData")
//    public Map<String, Object> getInventory(
//            @RequestParam("draw") int draw,
//            @RequestParam("start") int start,
//            @RequestParam("length") int length,
//            @RequestParam("search[value]") String searchValue,
//            @RequestParam("order[0][column]") int orderColumn,
//            @RequestParam("order[0][dir]") String orderDir) {
//
//        int page = start / length;
//        Sort.Direction sortDirection = orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        String[] columnNames = {"inventoryId", "productName", "quantity", "inventoryStatus", "completedDate"};
//        Sort sort = Sort.by(sortDirection, columnNames[orderColumn]);
//        Pageable pageable = PageRequest.of(page, length, sort);
//
//        Page<Inventory> inventoryPage;
//        if (searchValue == null || searchValue.isEmpty()) {
//            inventoryPage = inventoryRepository.findAll(pageable);
//        } else {
//            inventoryPage = inventoryRepository.findAllByProductNameContaining(searchValue, pageable);
//        }
//
//        List<Map<String, Object>> inventoriesData = inventoryPage.stream().map(inventory -> {
//            Map<String, Object> inventoryData = new HashMap<>();
//            inventoryData.put("inventoryId", inventory.getInventoryId());
//            inventoryData.put("productName", inventory.getProduct().getProductName());
//            inventoryData.put("quantity", inventory.getQuantity());
//            inventoryData.put("inventoryStatus", inventory.getInventoryStatus());
//            // inventoryData.put("completedDate", inventory.getCompletedDate());
//            return inventoryData;
//        }).collect(Collectors.toList());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("draw", draw);
//        response.put("recordsTotal", inventoryRepository.count());
//        response.put("recordsFiltered", inventoryPage.getTotalElements());
//        response.put("data", inventoriesData);
//
//        return response;
//    }

    @PostMapping("/inventoryData")
    public Map<String, Object> getInventory(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam("search[value]") String searchValue,
            @RequestParam Map<String, String> requestParams) {

        int page = start / length;
        List<Sort.Order> orders = new ArrayList<>();

        int i = 0;
        while (requestParams.containsKey("order[" + i + "][column]")) {
            int columnIdx = Integer.parseInt(requestParams.get("order[" + i + "][column]"));
            String columnName = getColumnName(columnIdx);
            String direction = requestParams.get("order[" + i + "][dir]");
            Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(new Sort.Order(sortDirection, columnName));
            i++;
        }

        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(page, length, sort);

        Page<Inventory> inventoryPage;
        if (searchValue == null || searchValue.isEmpty()) {
            inventoryPage = inventoryRepository.findAll(pageable);
        } else {
            inventoryPage = inventoryRepository.findAllByProduct_ProductNameContaining(searchValue, pageable);
        }

        List<Map<String, Object>> inventoriesData = inventoryPage.stream().map(inventory -> {
            Map<String, Object> inventoryData = new HashMap<>();
            inventoryData.put("inventoryId", inventory.getInventoryId());
            inventoryData.put("productName", inventory.getProduct().getProductName());
            inventoryData.put("quantity", inventory.getQuantity());
            inventoryData.put("inventoryStatus", inventory.getInventoryStatus());
             inventoryData.put("completedDate", inventory.getWorkPlan().getEnd()); // Uncomment if you have this field
            return inventoryData;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", inventoryRepository.count());
        response.put("recordsFiltered", inventoryPage.getTotalElements());
        response.put("data", inventoriesData);

        return response;
    }

    private String getColumnName(int columnIdx) {
        String[] columnNames = {"inventoryId", "product.productName", "quantity", "inventoryStatus", "completedDate"};
        return columnNames[columnIdx];
    }

    @GetMapping("/api/inventory/totalStock")
    public Map<String, Integer> getTotalStock() {
        return inventoryService.getTotalStockByProductNameAndStatus();
    }
    @GetMapping("/api/inventory/materialTotalStock")
    public Map<String, Integer> getMaterialTotalStock() {
        return inventoryService.getMaterialTotalStockByProductNameAndStatus();
    }
}
