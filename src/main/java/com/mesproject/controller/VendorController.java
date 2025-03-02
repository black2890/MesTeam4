package com.mesproject.controller;

import com.mesproject.entity.Vendor;
import com.mesproject.constant.vendorType;
import com.mesproject.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/delete-vendor")
    public ResponseEntity<String> deleteVendor(@RequestParam Long vendorId) {
        try {
            Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new Exception("Vendor not found"));
            vendor.setDeleted(true);
            vendorRepository.save(vendor);
            return ResponseEntity.ok("Vendor deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting vendor");
        }
    }

    // 업체 모든 데이터 조회
//    @PostMapping("/vendorData")
//    public Map<String, Object> getVendors(
//            @RequestParam("draw") int draw,
//            @RequestParam("start") int start,
//            @RequestParam("length") int length,
//            @RequestParam("search[value]") String searchValue,
//            @RequestParam("order[0][column]") int orderColumn,
//            @RequestParam("order[0][dir]") String orderDir) {
//
//        int page = start / length;
//        Sort.Direction sortDirection = orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        String[] columnNames = {"vendorId", "vendorName", "vendorType", "contactInfo"};
//        Sort sort = Sort.by(sortDirection, columnNames[orderColumn]);
//        Pageable pageable = PageRequest.of(page, length, sort);
//
//        Page<Vendor> vendorPage;
//        if (searchValue == null || searchValue.isEmpty()) {
//            vendorPage = vendorRepository.findAll(pageable);
//        } else {
//            vendorPage = vendorRepository.findAllByVendorNameContaining(searchValue, pageable);
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("draw", draw);
//        response.put("recordsTotal", vendorRepository.count());
//        response.put("recordsFiltered", vendorPage.getTotalElements());
//        response.put("data", vendorPage.getContent());
//
//        return response;
//    }

    // 논리적 삭제안된 업체정보 조회
    @PostMapping("/vendorData")
    public Map<String, Object> getVendors(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam("search[value]") String searchValue,
            @RequestParam("order[0][column]") int orderColumn,
            @RequestParam("order[0][dir]") String orderDir) {

        int page = start / length;
        Sort.Direction sortDirection = orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String[] columnNames = {"vendorId", "vendorName", "vendorType", "contactInfo"};
        Sort sort = Sort.by(sortDirection, columnNames[orderColumn]);
        Pageable pageable = PageRequest.of(page, length, sort);

        Page<Vendor> vendorPage;
        if (searchValue == null || searchValue.isEmpty()) {
            vendorPage = vendorRepository.findAllByDeletedFalse(pageable);
        } else {
            vendorPage = vendorRepository.findAllByVendorNameContainingAndDeletedFalse(searchValue, pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", vendorRepository.countByDeletedFalse());
        response.put("recordsFiltered", vendorPage.getTotalElements());
        response.put("data", vendorPage.getContent());

        return response;
    }

    @GetMapping("/get-vendors")
    public List<Vendor> getVendor() {
        return vendorRepository.findByVendorType(vendorType.ORDER);
    }


}