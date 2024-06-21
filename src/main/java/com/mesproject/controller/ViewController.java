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
    public String orders() {return "orders";}

    @GetMapping(value= "/inventorys")
    public String inventory() {return "inventory";}

    @GetMapping(value= "/inventoryChart")
    public String inventoryChart(){return "inventoryChart";}

    @GetMapping(value= "/p")
    public String summary(){return "productOrdersSummary";}

    @GetMapping(value= "/c")
    public String calendar(){return "workPlans";}
}
