package ru.supplyservice.productAccounting.api.response;

import java.math.BigDecimal;
import java.util.List;

public record StatInfo(
    String supplierName,
    List<AcceptedItemInfo> items,
    BigDecimal totalItemsQuantity,
    BigDecimal totalItemsPrice) {}
