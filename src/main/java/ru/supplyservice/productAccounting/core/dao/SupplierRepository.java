package ru.supplyservice.productAccounting.core.dao;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.supplyservice.productAccounting.core.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
  Optional<Supplier> findByName(String name);
}
