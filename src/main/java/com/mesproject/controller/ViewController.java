package com.mesproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping(value = "/vendor")
    public String vendor(){

        return "vendor";
    }

    @GetMapping(value= "/orders")
    public String orders() {return "orders2";}

    @GetMapping(value= "/inventorys")
    public String inventory() {return "inventory";}

    @GetMapping(value= "/inventoryChart")
    public String inventoryChart(){return "inventoryChart";}

    @GetMapping(value= "/p")
    public String summary(){return "productOrdersSummary";}

    @GetMapping(value= "/c")
    public String calendar(){return "workPlans";}

//    @GetMapping(value="/orders")
//    public String ordersPage() {
//
//        return "orders";
//    }

    @GetMapping(value="/equipment")
    public String equipmentsPage() {

        return "equipment";
    }

    @GetMapping(value="/materialOrders")
    public String materialOrdersPage() {

        return "materialOrders2";
    }

    @GetMapping(value="/materialInOut")
    public String materialInOutPage() {

        return "materialInOut2";
    }

    @GetMapping(value="/workOrders")
    public String workOrdersPage() {

        return "workOrders2";
    }

    //기준정보 View
    @GetMapping(value="/material")
    public String materialPage() {

        return "material";
    }

    @GetMapping(value="/process")
    public String processPage() {

        return "process";
    }

    @GetMapping(value="/product")
    public String productPage() {

        return "product";
    }

    @GetMapping(value="/routing")
    public String routingPage() {

        return "routing";
    }
}
