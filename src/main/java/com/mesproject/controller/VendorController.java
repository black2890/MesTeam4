package com.mesproject.controller;

import com.mesproject.entity.Vendor;
import com.mesproject.constant.vendorType;
import com.mesproject.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

    @PostMapping("/create-vendor")
    public Vendor createVendor(@RequestParam Map<String, String> body) {
        Vendor vendor = new Vendor();
        vendor.setVendorName(body.get("name")); // setVendorName 사용
        vendor.setVendorType(vendorType.valueOf(body.get("type").toUpperCase())); // setVendorType 사용
        vendor.setContactInfo(body.get("contact")); // setContactInfo 사용
        return vendorRepository.save(vendor);
    }

    @PostMapping("/vendorData")
    public Map<String, Object> getVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("data", vendors); // Datatables expects a "data" field
        return response;
    }

    @GetMapping("/get-vendors")
    public List<Vendor> getVendor() {
        return vendorRepository.findAll();
    }

//    private vendorType convertToVendorType(String type) {
//        switch (type.toLowerCase()) {
//            case "수주업체":
//                return vendorType.ORDER;
//            case "발주업체":
//                return vendorType.MATERIALORDER;
//            case "배송업체":
//                return vendorType.DELIVERY;
//            default:
//                throw new IllegalArgumentException("Invalid vendor type: " + type);
//        }
//    }
}