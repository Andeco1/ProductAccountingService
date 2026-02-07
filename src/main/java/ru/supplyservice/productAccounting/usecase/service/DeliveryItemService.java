package ru.supplyservice.productAccounting.usecase.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.supplyservice.productAccounting.core.dao.DeliveryItemRepository;
import ru.supplyservice.productAccounting.core.entity.DeliveryItem;
import ru.supplyservice.productAccounting.core.entity.DeliveryRecord;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.core.entity.Supplier;
import ru.supplyservice.productAccounting.usecase.dto.DeliveryItemDTO;
import ru.supplyservice.productAccounting.usecase.dto.ProductPricePeriodDTO;
import ru.supplyservice.productAccounting.usecase.mapper.DTOMapper;

@Service
public class DeliveryItemService {
  ProductPricePeriodService productPricePeriodService;
  ProductService productService;
  DTOMapper mapper;
  DeliveryItemRepository deliveryItemRepository;

  @Autowired
  public DeliveryItemService(
      ProductPricePeriodService productPricePeriodService,
      ProductService productService,
      DTOMapper mapper,
      DeliveryItemRepository deliveryItemRepository) {
    this.productPricePeriodService = productPricePeriodService;
    this.productService = productService;
    this.mapper = mapper;
    this.deliveryItemRepository = deliveryItemRepository;
  }

  @Transactional(readOnly = true)
  public List<DeliveryItemDTO> getAcceptedDeliveryItemsOfPeriod(Instant dateFrom, Instant dateTo) {
    return deliveryItemRepository
        .findAllByDeliveryRecordDateBetweenAndAcceptanceTrue(dateFrom, dateTo)
        .stream()
        .map(mapper::deliveryItemToDeliveryItemDTO)
        .toList();
  }

  @Transactional
  public DeliveryItem saveDeliveryItem(
      DeliveryRecord deliveryRecord, String productName, BigDecimal quantity, boolean acceptance) {
    Supplier supplier = deliveryRecord.getSupplier();
    Instant date = deliveryRecord.getDate();
    Product product = mapper.productDTOToProduct(productService.getProductByName(productName));
    ProductPricePeriodDTO productPricePeriodDTO =
        productPricePeriodService.getActivePricePeriod(product, supplier, date);
    BigDecimal priceOfAccepted = null;
    if (acceptance && productPricePeriodDTO != null) {
      priceOfAccepted = quantity.multiply(productPricePeriodDTO.pricePerMeasurementUnit());
    }
    DeliveryItem deliveryItem =
        new DeliveryItem(product, deliveryRecord, quantity, acceptance, priceOfAccepted);
    return deliveryItemRepository.save(deliveryItem);
  }
}
