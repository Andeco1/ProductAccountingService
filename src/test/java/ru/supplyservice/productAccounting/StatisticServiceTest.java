package ru.supplyservice.productAccounting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.supplyservice.productAccounting.api.response.ReportResponse;
import ru.supplyservice.productAccounting.api.response.StatInfo;
import ru.supplyservice.productAccounting.usecase.dto.DeliveryItemDTO;
import ru.supplyservice.productAccounting.usecase.dto.DeliveryRecordDTO;
import ru.supplyservice.productAccounting.usecase.dto.ProductDTO;
import ru.supplyservice.productAccounting.usecase.dto.SupplierDTO;
import ru.supplyservice.productAccounting.usecase.service.DeliveryItemService;
import ru.supplyservice.productAccounting.usecase.service.StatisticService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {

    @Mock
    private DeliveryItemService deliveryItemService;

    @InjectMocks
    private StatisticService service;

    @Test
    @DisplayName("Расчёт статистики")
    void getReportOfPeriod_CalculationCheck() {
        Instant start = Instant.now().minusSeconds(1000);
        Instant end = Instant.now();
        
        SupplierDTO supplier = new SupplierDTO(UUID.randomUUID(), "Sup1", "Addr");
        DeliveryRecordDTO record = new DeliveryRecordDTO(supplier, start, "info");
        ProductDTO product = new ProductDTO(1L, "Prod1", "i", "kg");

        DeliveryItemDTO item1 = new DeliveryItemDTO(record, product, BigDecimal.TEN, true, BigDecimal.valueOf(100));
        DeliveryItemDTO item2 = new DeliveryItemDTO(record, product, BigDecimal.valueOf(5), true, BigDecimal.valueOf(50));

        when(deliveryItemService.getAcceptedDeliveryItemsOfPeriod(start, end))
                .thenReturn(List.of(item1, item2));

        ReportResponse response = service.getReportOfPeriod(start, end);

        BigDecimal expectedTotalQty = BigDecimal.valueOf(15);
        assertEquals(response.quantityOfPeriod(), expectedTotalQty);
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(150);
        assertEquals(response.priceOfPeriod(), expectedTotalPrice);

        assertEquals(1, response.statistics().size());
        StatInfo statInfo = response.statistics().getFirst();
        assertEquals("Sup1", statInfo.supplierName());

        assertEquals(BigDecimal.valueOf(15), statInfo.totalItemsQuantity());
    }

    @Test
    @DisplayName("Расчёт статистики по пустой записи")
    void getReportOfPeriod_Empty() {
        Instant now = Instant.now();
        when(deliveryItemService.getAcceptedDeliveryItemsOfPeriod(now, now)).thenReturn(Collections.emptyList());

        ReportResponse response = service.getReportOfPeriod(now, now);

        assertEquals(BigDecimal.ZERO, response.quantityOfPeriod());
        assertEquals(BigDecimal.ZERO, response.priceOfPeriod());
        assertTrue(response.statistics().isEmpty());
    }
}