package com.mesproject.controller;

import com.mesproject.entity.MaterialInOut;
import com.mesproject.entity.MaterialOrders;
import com.mesproject.entity.Orders;
import com.mesproject.entity.OrdersMaterials;
import com.mesproject.repository.MaterialInOutRepository;
import com.mesproject.repository.MaterialOrdersRepository;
import com.mesproject.service.MaterialInOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MaterialOrderController {

    private final MaterialOrdersRepository materialOrdersRepository;
    private final MaterialInOutRepository materialInOutRepository;
    private final MaterialInOutService materialInOutService;

    @GetMapping("/data/materialOrders")
    public String materialOrders(Model model){


        List<MaterialOrders> materialOrders = materialOrdersRepository.findAll();
        model.addAttribute("materialOrders", materialOrders);
        return "materialOrders";
    }

    @PostMapping("/material/in")
    public ResponseEntity<?> materialsIn(@RequestBody Long materialOrderId){
        materialInOutService.In(materialOrderId);

        return ResponseEntity.ok()
                .build();
    }
    @GetMapping("/data/materialInOut")
    public String materialInOut(Model model){


        List<MaterialInOut> materialInOuts = materialInOutRepository.findAll();
        model.addAttribute("materialInOut", materialInOuts);
        return "materialInOut";
    }


//    @PostMapping("/data/materialOrders")
//    public Map<String, Object> getOrders(
//            @RequestParam("draw") int draw,
//            @RequestParam("start") int start,
//            @RequestParam("length") int length,
//            @RequestParam("search[value]") String searchValue,
//            @RequestParam Map<String, String> requestParams) {
//
//
//        int page = start / length;
//        List<Sort.Order> orders = new ArrayList<>();
//
//        // Collect sorting information
//        int i = 0;
//        while (requestParams.containsKey("order[" + i + "][column]")) {
//            int columnIdx = Integer.parseInt(requestParams.get("order[" + i + "][column]")) ;
//            String columnName = getOrderColumnName(columnIdx);
//            String direction = requestParams.get("order[" + i + "][dir]");
//            Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//            orders.add(new Sort.Order(sortDirection, columnName));
//            i++;
//        }
//
//        if (orders.isEmpty()) {
//            orders.add(new Sort.Order(Sort.Direction.DESC, "orderId"));
//        }
//
//        Sort sort = Sort.by(orders);
//        Pageable pageable = PageRequest.of(page, length, sort);
//
//        Page<MaterialOrders> ordersPage = null;
//        if (searchValue == null || searchValue.isEmpty()) {
//            ordersPage = materialOrdersRepository.findAll(pageable);
//        } else {
////            ordersPage = materialOrdersRepository.findAllByProduct_ProductNameContainingOrVendor_VendorNameContaining(searchValue, searchValue, pageable);
//        }
//
////        Page<Orders> ordersPage;
////        if (searchValue == null || searchValue.isEmpty()) {
////            ordersPage = ordersRepository.findAll(pageable);
////        } else {
////            ordersPage = ordersRepository.findAllByProductNameOrVendorNameOrDeliveryDateOrOrdersStatus(
////                    searchValue, searchValue, searchValue, searchValue, pageable);
////        }
//
//        List<Map<String, Object>> ordersData = new ArrayList<>();
//        for (MaterialOrders materialOrders : ordersPage.getContent()) {
//            Map<String, Object> orderData = new HashMap<>();
//            orderData.put("orderId", materialOrders.getMaterialOrderId());
//            orderData.put("productName", materialOrders.getMaterialOrderDate());
//            orderData.put("vendorName", materialOrders.getProduct().getProductName());
//            orderData.put("quantity", materialOrders.getQuantity());
//            orderData.put("deliveryDate", materialOrders.getDeliveryDate());
//            orderData.put("deliveryAddress", materialOrders.getMaterialOrdersStatus());
//        //    orderData.put("ordersStatus", order.getOrdersStatus());
//            ordersData.add(orderData);
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("draw", draw);
//        response.put("recordsTotal", materialOrdersRepository.count());
//        response.put("recordsFiltered", ordersPage.getTotalElements());
//        response.put("data", ordersData);
//
//        return response;
//    }
//    private String getOrderColumnName(int columnIdx) {
//        String[] columnNames = {"orderId", "product.productName", "vendor.vendorName", "quantity", "deliveryDate", "deliveryAddress"};
//        return columnNames[columnIdx];
//    }


}
