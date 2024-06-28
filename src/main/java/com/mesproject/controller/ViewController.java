package com.mesproject.controller;

import com.mesproject.dto.WorkPlanProgressDto;
import com.mesproject.service.ProcessInfoService;
import com.mesproject.service.WorkPlanService;
import com.mesproject.constant.vendorType;
import com.mesproject.repository.InventoryRepository;
import com.mesproject.repository.VendorRepository;
import com.mesproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ViewController {
    private final OrdersService ordersService;
    private final VendorRepository vendorRepository;
    private final InventoryRepository inventoryRepository;

    private final ProcessInfoService processInfoService;
    private final WorkPlanService workPlanService;

    @GetMapping("/main")
    public String workPlanProgress(Model model) {

        List<WorkPlanProgressDto> workPlanProgresses = workPlanService.getWorkPlanProgresses();

        model.addAttribute("workPlanProgresses", workPlanProgresses);
        return "main";
    }

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
        System.out.println("1");

        List<String> processList = processInfoService.getProcessList();
        model.addAttribute("btnText", processList.get(0));
        model.addAttribute("processList", processList);
        return "processInfo";

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
    @GetMapping(value = "/data/inventories")
    public String inventories(Model model) {
        model.addAttribute("inventories",inventoryRepository.findAll());
        return "inventory2";
    }
}
