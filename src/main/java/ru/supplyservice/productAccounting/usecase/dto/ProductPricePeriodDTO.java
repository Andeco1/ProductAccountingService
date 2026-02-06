package ru.supplyservice.productAccounting.usecase.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductPricePeriodDTO(
        String supplierName,
        String productName,
        Instant startDate,
        Instant endDate,
        BigDecimal pricePerMeasurementUnit
) {
}
