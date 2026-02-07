package ru.supplyservice.productAccounting.api.response;

import java.math.BigDecimal;
import java.util.List;

public record ReportResponse(
        BigDecimal quantityOfPeriod,
        BigDecimal priceOfPeriod,
        List<StatInfo> statistics
) {
}
