package com.mesproject.repository;

import com.mesproject.entity.MaterialOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialOrdersRepository extends JpaRepository<MaterialOrders, Long> {
}
