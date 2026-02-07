package ru.supplyservice.productAccounting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.supplyservice.productAccounting.core.dao.ProductPricePeriodRepository;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.core.entity.ProductPricePeriod;
import ru.supplyservice.productAccounting.core.entity.Supplier;
import ru.supplyservice.productAccounting.exception.NoActivePriceFoundException;
import ru.supplyservice.productAccounting.usecase.dto.ProductPricePeriodDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;
import ru.supplyservice.productAccounting.usecase.service.ProductPricePeriodService;
import ru.supplyservice.productAccounting.usecase.service.ProductService;
import ru.supplyservice.productAccounting.usecase.service.SupplierService;

@ExtendWith(MockitoExtension.class)
class ProductPricePeriodServiceTest {

  @Mock private ProductPricePeriodRepository repository;
  @Mock private DTOMapper mapper;
  @Mock private SupplierService supplierService;
  @Mock private ProductService productService;

  @InjectMocks private ProductPricePeriodService service;

  @Test
  @DisplayName("Найти запись с ценой в диапазоне")
  void getActivePricePeriod_Success() {
    Product p = new Product();
    p.setName("P");
    Supplier s = new Supplier();
    s.setName("S");
    Instant now = Instant.now();
    ProductPricePeriod ppp = new ProductPricePeriod();

    when(repository.findActivePricePeriod(p, s, now)).thenReturn(List.of(ppp));
    when(mapper.productPricePeriodToProductPricePeriodDTO(ppp))
        .thenReturn(new ProductPricePeriodDTO("S", "P", now, now, null));

    ProductPricePeriodDTO result = service.getActivePricePeriod(p, s, now);

    assertNotNull(result);
  }

  @Test
  @DisplayName("Цена по продукту не найдена")
  void getActivePricePeriod_NotFound() {
    Product p = new Product();
    p.setName("P");
    Supplier s = new Supplier();
    s.setName("S");
    Instant now = Instant.now();

    when(repository.findActivePricePeriod(p, s, now)).thenReturn(Collections.emptyList());

    assertThrows(NoActivePriceFoundException.class, () -> service.getActivePricePeriod(p, s, now));
  }

  @Test
  @DisplayName("Найдено две цены на один продукт и одного поставщика")
  void getActivePricePeriod_MultipleFound() {
    Product p = new Product();
    p.setName("P");
    Supplier s = new Supplier();
    s.setName("S");
    Instant now = Instant.now();

    when(repository.findActivePricePeriod(p, s, now))
        .thenReturn(List.of(new ProductPricePeriod(), new ProductPricePeriod()));

    assertThrows(NoActivePriceFoundException.class, () -> service.getActivePricePeriod(p, s, now));
  }
}
