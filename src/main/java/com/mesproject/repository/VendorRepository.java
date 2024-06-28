package com.mesproject.repository;

import com.mesproject.constant.vendorType;
import com.mesproject.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Vendor findByVendorName(String vendorName);

    Page<Vendor> findAllByVendorNameContaining(String searchValue, Pageable pageable);

    List<Vendor> findByVendorType(vendorType vendorType);

}
