package ru.supplyservice.productAccounting.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.supplyservice.productAccounting.core.entity.Product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findByNameIn(Collection<String> names);
}
