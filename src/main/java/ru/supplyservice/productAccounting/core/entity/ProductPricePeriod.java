package ru.supplyservice.productAccounting.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class ProductPricePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(nullable = false)
    Instant startDate;
    @Column(nullable = false)
    Instant endDate;
    @Column(nullable = false)
    BigDecimal pricePerMeasurementUnit;

    public ProductPricePeriod(Supplier supplier, Product product, Instant startDate, Instant endDate, BigDecimal pricePerMeasurementUnit) {
        this.supplier = supplier;
        this.product = product;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricePerMeasurementUnit = pricePerMeasurementUnit;
    }
}
