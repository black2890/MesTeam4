package com.mesproject.controller;

import com.mesproject.constant.vendorType;
import com.mesproject.repository.VendorRepository;
import com.mesproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ViewController {
    private final OrdersService ordersService;
    private final VendorRepository vendorRepository;

    @GetMapping(value = "/vendor")
    public String vendor() {
        return "vendor";
    }

    @GetMapping(value = "/orders")
    public String orders() {
        return "orders";
    }

    @GetMapping(value = "/inventorys")
    public String inventory() {
        return "inventory";
    }

    @GetMapping(value = "/inventoryChart")
    public String inventoryChart() {
        return "inventoryChart";
    }

    @GetMapping(value = "/MaterialInventoryChart")
    public String MaterialInventoryChart() {
        return "MaterialInventoryChart";
    }

    @GetMapping(value = "/p")
    public String summary() {
        return "productOrdersSummary";
    }

    @GetMapping(value = "/c")
    public String calendar() {
        return "workPlans";
    }

    @GetMapping(value = "/pc")
    public String pc() {
        return "productChart";
    }

    @GetMapping(value = "/upload")
    public String upload() {
        return "excel";
    }
    @GetMapping("/data/shipments")
    public String getCompletedOrders(Model model) {
        model.addAttribute("shipments", ordersService.getCompletedOrders());
        model.addAttribute("vendors",vendorRepository.findByVendorType(vendorType.DELIVERY));

        return "shipment";
    }
}
