package ru.supplyservice.productAccounting.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.core.entity.ProductPricePeriod;
import ru.supplyservice.productAccounting.core.entity.Supplier;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ProductPricePeriodRepository extends JpaRepository<ProductPricePeriod, Long> {
    @Query("""
        select ppp
        from ProductPricePeriod ppp
        where ppp.product = :product
          and ppp.supplier = :supplier
          and :date between ppp.startDate and ppp.endDate
    """)
    Optional<ProductPricePeriod> findActivePricePeriod(
            Product product,
            Supplier supplier,
            Instant date
    );
}
