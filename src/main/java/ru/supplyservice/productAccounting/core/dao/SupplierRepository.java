package ru.supplyservice.productAccounting.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.supplyservice.productAccounting.core.entity.Supplier;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    Optional<Supplier> findByName(String name);
}
