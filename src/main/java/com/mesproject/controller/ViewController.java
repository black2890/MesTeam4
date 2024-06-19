package com.mesproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping(value = "/")
    public String vendor(){

        return "vendor";
    }

    @GetMapping(value="/orders")
    public String ordersPage() {

        return "orders";
    }

    @GetMapping(value="/equipment")
    public String equipmentsPage() {

        return "equipment";
    }

    @GetMapping(value="/materialOrders")
    public String materialOrdersPage() {

        return "materialOrders";
    }

    @GetMapping(value="/materialInOut")
    public String materialInOutPage() {

        return "materialInOut";
    }

    @GetMapping(value="/workOrders")
    public String workOrdersPage() {

        return "materialInOut";
    }
}
