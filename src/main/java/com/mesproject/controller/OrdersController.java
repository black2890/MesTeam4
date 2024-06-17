//package com.mesproject.controller;
//
//import com.mesproject.entity.Orders;
//import com.mesproject.repository.OrdersRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class OrderController {
//
//    @Autowired
//    private OrdersRepository ordersRepository;
//
//    @PostMapping("/create-order")
//    public Orders createOrder(@RequestParam Map<String, String> body) {
//        Orders order = new Orders();
//        order.setProductName(body.get("productName"));
//        order.setVendorName(body.get("vendorName"));
//        order.setDeliveryAddress(body.get("deliveryAddress"));
//        order.setDeliveryDate(new Date(Long.parseLong(body.get("deliveryDate")))); // Assuming the date is sent as a timestamp
//        order.setStatus("Pending"); // Set a default status
//        return ordersRepository.save(order);
//    }
//
//    @PostMapping("/dataOrders")
//    public Map<String, Object> getOrders() {
//        List<Orders> orders = ordersRepository.findAll();
//        Map<String, Object> response = new HashMap<>();
//        response.put("data", orders); // Datatables expects a "data" field
//        return response;
//    }
//}
