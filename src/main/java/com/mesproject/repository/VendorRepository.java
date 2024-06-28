package com.mesproject.repository;

import com.mesproject.constant.vendorType;
import com.mesproject.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Vendor findByVendorName(String vendorName);

    Page<Vendor> findAllByVendorNameContaining(String searchValue, Pageable pageable);

    List<Vendor> findByVendorType(vendorType vendorType);

    @Query("SELECT v FROM Vendor v WHERE v.deleted = false")
    List<Vendor> findAllNotDeleted();

    Page<Vendor> findAllByDeletedFalse(Pageable pageable);

    Page<Vendor> findAllByVendorNameContainingAndDeletedFalse(String vendorName, Pageable pageable);

    long countByDeletedFalse();
}
