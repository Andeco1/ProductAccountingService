package ru.supplyservice.productAccounting.api.request;

import java.math.BigDecimal;

public record ItemsInfo(
        String productName,
        BigDecimal quantity,
        Boolean acceptance
) {
}
