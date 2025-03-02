package com.mesproject.controller;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.constant.vendorType;
import com.mesproject.dto.OrderDto;
import com.mesproject.dto.OrderStatusUpdateRequest;
import com.mesproject.dto.ShipmentDto;
import com.mesproject.dto.WorkOrdersDto;
import com.mesproject.entity.Orders;
import com.mesproject.entity.Product;
import com.mesproject.entity.Vendor;
import com.mesproject.dto.OrderDetailsDto;
import com.mesproject.dto.OrderDto;
import com.mesproject.dto.OrderStatusUpdateRequest;
import com.mesproject.entity.*;
import com.mesproject.repository.OrdersRepository;
import com.mesproject.repository.ProductRepository;
import com.mesproject.repository.VendorRepository;
import com.mesproject.service.OrderService;
import com.mesproject.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    @Autowired
    private OrderService orderService;

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

//    @PostMapping("/create-order")
//    public Orders createOrder(@RequestParam Map<String, String> body){
//
//        Orders order = new Orders();
//        System.out.println(body.get("productName"));
//        Product product = productRepository.findByProductName(body.get("productName"));
//        System.out.println(product);
//        Vendor vendor = vendorRepository.findByVendorName(body.get("vendorName"));
//
//        if (product == null || vendor == null) {
//            throw new RuntimeException("Invalid product or vendor name");
//        }
//
//        order.setProduct(product);
//        order.setVendor(vendor);
//        order.setQuantity((long) Integer.parseInt(body.get("quantity")));
//        order.setDeliveryDate(LocalDate.parse(body.get("deliveryDate"),
//                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        order.setDeliveryAddress(body.get("deliveryAddress"));
//        order.setOrdersStatus(OrdersStatus.PENDINGSTORAGE);
//        return ordersRepository.save(order);
//    }


    @PostMapping("/create-order")
    public void createOrder(@RequestParam Map<String, String> body){

        Orders order = new Orders();

        Product product = productRepository.findByProductName(body.get("productName"));
        Vendor vendor = vendorRepository.findByVendorName(body.get("vendorName"));

        if (product == null || vendor == null) {
            throw new RuntimeException("Invalid product or vendor name");
        }

        order.setProduct(product);
        order.setVendor(vendor);
        order.setQuantity((long) Integer.parseInt(body.get("quantity")));
        order.setDeliveryDate(LocalDate.parse(body.get("deliveryDate"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
        order.setDeliveryAddress(body.get("deliveryAddress"));
        order.setOrdersStatus(OrdersStatus.PENDINGSTORAGE);

        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(product.getProductId());
        orderDto.setVendorId(vendor.getVendorId());
        orderDto.setQuantity((long) Integer.parseInt(body.get("quantity")));
        orderDto.setDeliveryDate(LocalDate.parse(body.get("deliveryDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
        orderDto.setDeliveryAddress(body.get("deliveryAddress"));

        orderService.order(orderDto);

    }
    @PostMapping("/updateOrderStatus")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderStatusUpdateRequest request) {
        // request에서 orderIds와 status를 가져와서 처리
        List<Long> orderIds = request.getOrderIds();
        Enum status = request.getStatus();

        // 여기서 orderService를 사용하여 주문 상태를 업데이트
        ordersService.updateOrderStatus(orderIds, (OrdersStatus) status);


     //   ordersService.updateOrderStatus(request.getOrderIds(), request.getStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/product-orders-summary")
    public List<Object[]> getProductOrdersSummary() {
        return ordersService.getProductOrdersSummary();
    }
    @PostMapping("/get-estimated-date")
    public ResponseEntity<Map<String, String>> getEstimatedDate(@RequestBody Map<String, Object> request) {
        String productName = (String) request.get("productName");
        String temp = (String) request.get("quantity");
        int quantity = Integer.parseInt(temp);
        Product product = productRepository.findByProductName(productName);

        // 예상 납품 날짜를 계산하는 로직
        LocalDate estimatedDate = orderService.calculateEstimatedDate(product, quantity);

        // 응답으로 예상 납품 날짜를 반환
        Map<String, String> response = new HashMap<>();
        response.put("estimatedDate", estimatedDate.toString());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/upload-orders")
    public ResponseEntity<?> uploadOrders(@RequestParam("file") MultipartFile file) {
        try {
            List<OrderDto> orders = orderService.parseExcelFile(file);
            orders.forEach(orderService::order);
            return ResponseEntity.ok("Orders processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }



    @PostMapping("/shipment/start")
    public ResponseEntity<?> shipmentStart(@RequestBody ShipmentDto shipmentDto){

        ordersService.shipmentStart(shipmentDto);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/shipment/end")
    public ResponseEntity<?> shipmentEnd(@RequestBody ShipmentDto shipmentDto){

        ordersService.shipmentEnd(shipmentDto);
        return ResponseEntity.ok()
                .build();
    }


    @GetMapping("/api/orders/details")
    public ResponseEntity<List<Map<String, Object>>> getOrderDetails(@RequestParam("orderId") Long orderId) {
        List<Map<String, Object>> orderDetails = ordersService.getOrderDetails(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @GetMapping("/api/orders/related")
    public List<Map<String, Object>> getRelatedOrders(@RequestParam Long workPlanId) {
        return orderService.getRelatedOrdersByWorkPlanId(workPlanId);
    }
}
