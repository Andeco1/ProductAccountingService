package ru.supplyservice.productAccounting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.supplyservice.productAccounting.api.request.DeliveryRecordInfo;
import ru.supplyservice.productAccounting.api.request.ItemInfo;
import ru.supplyservice.productAccounting.core.dao.DeliveryItemRepository;
import ru.supplyservice.productAccounting.core.dao.DeliveryRecordRepository;
import ru.supplyservice.productAccounting.core.dao.ProductPricePeriodRepository;
import ru.supplyservice.productAccounting.core.dao.ProductRepository;
import ru.supplyservice.productAccounting.core.entity.*;
import ru.supplyservice.productAccounting.exception.NoActivePriceFoundException;
import ru.supplyservice.productAccounting.exception.ProductAccountingException;
import ru.supplyservice.productAccounting.usecase.dto.SupplierDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;
import ru.supplyservice.productAccounting.usecase.service.DeliveryRecordService;
import ru.supplyservice.productAccounting.usecase.service.SupplierService;

@ExtendWith(MockitoExtension.class)
class DeliveryRecordServiceTest {

  @Mock private SupplierService supplierService;
  @Mock private ProductRepository productRepository;
  @Mock private ProductPricePeriodRepository priceRepository;
  @Mock private DeliveryRecordRepository deliveryRecordRepository;
  @Mock private DeliveryItemRepository deliveryItemRepository;
  @Mock private DTOMapper mapper;

  @InjectMocks private DeliveryRecordService service;

  @Test
  @DisplayName("Успешно сохранить запись о поставке")
  void saveDeliveryRecord_Success() {
    String supplierName = "Best Fruits";
    Instant now = Instant.now();
    String prodName = "Apple";

    ItemInfo itemInfo = new ItemInfo(prodName, BigDecimal.TEN, true);
    DeliveryRecordInfo request =
        new DeliveryRecordInfo(supplierName, now, "Info", List.of(itemInfo));

    SupplierDTO supplierDTO = new SupplierDTO(UUID.randomUUID(), supplierName, "Addr");
    Supplier supplier = new Supplier();
    supplier.setName(supplierName);

    Product product = new Product();
    product.setName(prodName);

    ProductPricePeriod pricePeriod = new ProductPricePeriod();
    pricePeriod.setProduct(product);
    pricePeriod.setPricePerMeasurementUnit(BigDecimal.valueOf(50));

    when(supplierService.getSupplierByName(supplierName)).thenReturn(supplierDTO);
    when(mapper.supplierDTOToSupplier(supplierDTO)).thenReturn(supplier);
    when(deliveryRecordRepository.save(any(DeliveryRecord.class)))
        .thenAnswer(i -> i.getArguments()[0]);
    when(productRepository.findByNameIn(Set.of(prodName))).thenReturn(List.of(product));
    when(priceRepository.findAllActivePrices(supplier, now)).thenReturn(List.of(pricePeriod));

    service.saveDeliveryRecord(request);

    verify(deliveryItemRepository).saveAll(anyList());

    ArgumentCaptor<List<DeliveryItem>> captor = ArgumentCaptor.forClass(List.class);
    verify(deliveryItemRepository).saveAll(captor.capture());

    DeliveryItem savedItem = captor.getValue().getFirst();
    assertEquals(prodName, savedItem.getProduct().getName());
    assertEquals(BigDecimal.valueOf(500), savedItem.getPriceOfAccepted());
  }

  @Test
  @DisplayName("Отсутствие товара в БД")
  void saveDeliveryRecord_ProductMissing() {
    DeliveryRecordInfo request =
        new DeliveryRecordInfo(
            "Sup",
            Instant.now(),
            "Inf",
            List.of(new ItemInfo("UnknownFruit", BigDecimal.ONE, true)));

    SupplierDTO supplierDTO = new SupplierDTO(UUID.randomUUID(), "Sup", "A");
    when(supplierService.getSupplierByName("Sup")).thenReturn(supplierDTO);
    when(mapper.supplierDTOToSupplier(supplierDTO)).thenReturn(new Supplier());

    when(productRepository.findByNameIn(any())).thenReturn(Collections.emptyList());

    assertThrows(ProductAccountingException.class, () -> service.saveDeliveryRecord(request));
  }

  @Test
  @DisplayName("Нет цены на период")
  void saveDeliveryRecord_NoPriceForAccepted() {
    String prodName = "Banana";
    Instant now = Instant.now();
    Supplier supplier = new Supplier();

    ItemInfo itemInfo = new ItemInfo(prodName, BigDecimal.ONE, true);
    DeliveryRecordInfo request = new DeliveryRecordInfo("Sup", now, "Inf", List.of(itemInfo));

    Product product = new Product();
    product.setName(prodName);

    when(supplierService.getSupplierByName(any()))
        .thenReturn(new SupplierDTO(UUID.randomUUID(), "Sup", "A"));
    when(mapper.supplierDTOToSupplier(any())).thenReturn(supplier);
    when(deliveryRecordRepository.save(any())).thenReturn(new DeliveryRecord());
    when(productRepository.findByNameIn(any())).thenReturn(List.of(product));
    when(priceRepository.findAllActivePrices(supplier, now)).thenReturn(Collections.emptyList());

    NoActivePriceFoundException ex =
        assertThrows(NoActivePriceFoundException.class, () -> service.saveDeliveryRecord(request));
    assertTrue(ex.getMessage().contains("Нет цены для"));
  }

  @Test
  @DisplayName("Сохранить информацию о поставке даже, если товар не принят")
  void saveDeliveryRecord_UnacceptedItem_NoPriceNeeded() {
    String prodName = "RottenApple";
    ItemInfo itemInfo = new ItemInfo(prodName, BigDecimal.TEN, false);

    DeliveryRecordInfo request =
        new DeliveryRecordInfo("Sup", Instant.now(), "Inf", List.of(itemInfo));
    Product product = new Product();
    product.setName(prodName);

    when(supplierService.getSupplierByName(any()))
        .thenReturn(new SupplierDTO(UUID.randomUUID(), "Sup", "A"));
    when(mapper.supplierDTOToSupplier(any())).thenReturn(new Supplier());
    when(deliveryRecordRepository.save(any())).thenReturn(new DeliveryRecord());
    when(productRepository.findByNameIn(any())).thenReturn(List.of(product));

    when(priceRepository.findAllActivePrices(any(), any())).thenReturn(Collections.emptyList());

    service.saveDeliveryRecord(request);

    ArgumentCaptor<List<DeliveryItem>> captor = ArgumentCaptor.forClass(List.class);
    verify(deliveryItemRepository).saveAll(captor.capture());

    assertNull(captor.getValue().getFirst().getPriceOfAccepted());
    assertFalse(captor.getValue().getFirst().getAcceptance());
  }
}
