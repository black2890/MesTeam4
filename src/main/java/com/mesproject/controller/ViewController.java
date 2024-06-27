package com.mesproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

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
}
