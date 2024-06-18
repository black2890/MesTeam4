package com.mesproject.controller;

import com.mesproject.constant.OrdersStatus;
import com.mesproject.entity.Orders;
import com.mesproject.entity.Product;
import com.mesproject.entity.Vendor;
import com.mesproject.repository.OrdersRepository;
import com.mesproject.repository.ProductRepository;
import com.mesproject.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrdersController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;


    @PostMapping("/orderData")
    public Map<String, Object> getOrders() {
        List<Orders> orders = ordersRepository.findAll();
        List<Map<String, Object>> ordersData = new ArrayList<>();
        for (Orders order : orders) {
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
        response.put("data", ordersData); // Datatables expects a "data" field
        return response;
    }

    @PostMapping("/create-order")
    public Orders createOrder(@RequestParam Map<String, String> body){

        Orders order = new Orders();

        Product product = productRepository.findByProductName(body.get("productName"));
        Vendor vendor = vendorRepository.findByVendorName(body.get("vendorName"));

        if (product == null || vendor == null) {
            throw new RuntimeException("Invalid product or vendor name");
        }

        order.setProduct(product);
        order.setVendor(vendor);
        order.setQuantity((long) Integer.parseInt(body.get("quantity")));
        order.setDeliveryDate(LocalDate.parse(body.get("deliveryDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
        order.setDeliveryAddress(body.get("deliveryAddress"));
        order.setOrdersStatus(OrdersStatus.PENDINGSTORAGE);
        return ordersRepository.save(order);
    }
}
