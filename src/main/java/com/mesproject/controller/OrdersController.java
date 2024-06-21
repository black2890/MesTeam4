package com.mesproject.controller;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.dto.OrderStatusUpdateRequest;
import com.mesproject.entity.Orders;
import com.mesproject.entity.Product;
import com.mesproject.entity.Vendor;
import com.mesproject.repository.OrdersRepository;
import com.mesproject.repository.ProductRepository;
import com.mesproject.repository.VendorRepository;
import com.mesproject.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class OrdersController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/orderData")
    public Map<String, Object> getOrders(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam("search[value]") String searchValue,
            @RequestParam Map<String, String> requestParams) {

        int page = start / length;
        List<Sort.Order> orders = new ArrayList<>();

        // Collect sorting information
        int i = 0;
        while (requestParams.containsKey("order[" + i + "][column]")) {
            int columnIdx = Integer.parseInt(requestParams.get("order[" + i + "][column]")) ;
            String columnName = getOrderColumnName(columnIdx);
            String direction = requestParams.get("order[" + i + "][dir]");
            Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(new Sort.Order(sortDirection, columnName));
            i++;
        }

        if (orders.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "orderId"));
        }

        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(page, length, sort);

        Page<Orders> ordersPage;
        if (searchValue == null || searchValue.isEmpty()) {
            ordersPage = ordersRepository.findAll(pageable);
        } else {
            ordersPage = ordersRepository.findAllByProduct_ProductNameContainingOrVendor_VendorNameContaining(searchValue, searchValue, pageable);
        }

//        Page<Orders> ordersPage;
//        if (searchValue == null || searchValue.isEmpty()) {
//            ordersPage = ordersRepository.findAll(pageable);
//        } else {
//            ordersPage = ordersRepository.findAllByProductNameOrVendorNameOrDeliveryDateOrOrdersStatus(
//                    searchValue, searchValue, searchValue, searchValue, pageable);
//        }

        List<Map<String, Object>> ordersData = new ArrayList<>();
        for (Orders order : ordersPage.getContent()) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getOrderId());
            orderData.put("productName", order.getProduct().getProductName());
            orderData.put("vendorName", order.getVendor().getVendorName());
            orderData.put("quantity", order.getQuantity());
            orderData.put("deliveryDate", order.getDeliveryDate());
            orderData.put("deliveryAddress", order.getDeliveryAddress());
            orderData.put("ordersStatus", order.getOrdersStatus());
            ordersData.add(orderData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", ordersRepository.count());
        response.put("recordsFiltered", ordersPage.getTotalElements());
        response.put("data", ordersData);

        return response;
    }

    private String getOrderColumnName(int columnIdx) {
        String[] columnNames = {"orderId", "product.productName", "vendor.vendorName", "quantity", "deliveryDate", "deliveryAddress", "ordersStatus"};
        return columnNames[columnIdx];
    }

    @PostMapping("/create-order")
    public Orders createOrder(@RequestParam Map<String, String> body){

        Orders order = new Orders();
        System.out.println(body.get("productName"));
        Product product = productRepository.findByProductName(body.get("productName"));
        System.out.println(product);
        Vendor vendor = vendorRepository.findByVendorName(body.get("vendorName"));

        if (product == null || vendor == null) {
            throw new RuntimeException("Invalid product or vendor name");
        }

        order.setProduct(product);
        order.setVendor(vendor);
        order.setQuantity((long) Integer.parseInt(body.get("quantity")));
        order.setDeliveryDate(LocalDate.parse(body.get("deliveryDate"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        order.setDeliveryAddress(body.get("deliveryAddress"));
        order.setOrdersStatus(OrdersStatus.PENDINGSTORAGE);
        return ordersRepository.save(order);
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderStatusUpdateRequest request) {
        // request에서 orderIds와 status를 가져와서 처리
        List<Long> orderIds = request.getOrderIds();
        Enum status = request.getStatus();

        // 여기서 orderService를 사용하여 주문 상태를 업데이트
        ordersService.updateOrderStatus(orderIds, (OrdersStatus) status);


        ordersService.updateOrderStatus(request.getOrderIds(), request.getStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/product-orders-summary")
    public List<Object[]> getProductOrdersSummary() {
        return ordersService.getProductOrdersSummary();
    }
}
