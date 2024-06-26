package com.mesproject.controller;

import com.mesproject.service.ProcessInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ViewController {

    private final ProcessInfoService processInfoService;

    @GetMapping(value = "/")
    public String vendor(){

        return "vendor";
    }

    @GetMapping(value= "/inventorys")
    public String inventory() {return "inventory";}

    @GetMapping(value= "/inventoryChart")
    public String inventoryChart(){return "inventoryChart";}

    @GetMapping(value= "/p")
    public String summary(){return "productOrdersSummary";}

    @GetMapping(value= "/c")
    public String calendar(){return "workPlans";}

    @GetMapping(value= "/pc")
    public String pc() {return "productChart";}
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

        return "workOrders";
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

    @GetMapping(value="/processByInfo")
    public String processInfoPage(Model model) {

        List<String> processList = processInfoService.getProcessList();
        model.addAttribute("btnText", processList.get(0));
        model.addAttribute("processList", processList);
        return "processInfo";
    }
}
