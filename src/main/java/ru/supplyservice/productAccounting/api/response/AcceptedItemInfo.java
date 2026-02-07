package ru.supplyservice.productAccounting.api.response;

import java.math.BigDecimal;

public record AcceptedItemInfo(String productName, BigDecimal quantity, BigDecimal price) {}
