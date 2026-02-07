package ru.supplyservice.productAccounting.usecase.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
public class DeliveryRecordService {
  private final SupplierService supplierService;
  private final ProductRepository productRepository;
  private final ProductPricePeriodRepository priceRepository;
  private final DeliveryRecordRepository deliveryRecordRepository;
  private final DeliveryItemRepository deliveryItemRepository;
  private final DTOMapper mapper;

  @Autowired
  public DeliveryRecordService(
      SupplierService supplierService,
      ProductRepository productRepository,
      ProductPricePeriodRepository priceRepository,
      DeliveryRecordRepository deliveryRecordRepository,
      DeliveryItemRepository deliveryItemRepository,
      DTOMapper mapper) {
    this.supplierService = supplierService;
    this.productRepository = productRepository;
    this.priceRepository = priceRepository;
    this.deliveryRecordRepository = deliveryRecordRepository;
    this.deliveryItemRepository = deliveryItemRepository;
    this.mapper = mapper;
  }

  @Transactional
  public void saveDeliveryRecord(DeliveryRecordInfo info) {
    SupplierDTO supplierDTO = supplierService.getSupplierByName(info.supplierName());
    Supplier supplier = mapper.supplierDTOToSupplier(supplierDTO);
    DeliveryRecord record = new DeliveryRecord(supplier, info.date(), info.info());
    record = deliveryRecordRepository.save(record);

    Set<String> productNames =
        info.items().stream().map(ItemInfo::productName).collect(Collectors.toSet());
    Map<String, Product> productMap =
        productRepository.findByNameIn(productNames).stream()
            .collect(Collectors.toMap(Product::getName, Function.identity()));

    if (productMap.size() != productNames.size()) {
      throw new ProductAccountingException("Часть продуктов не найдена в базе");
    }

    Map<Product, BigDecimal> priceMap =
        priceRepository.findAllActivePrices(supplier, info.date()).stream()
            .collect(
                Collectors.toMap(
                    ProductPricePeriod::getProduct,
                    ProductPricePeriod::getPricePerMeasurementUnit));
    List<DeliveryItem> itemsToSave = new ArrayList<>();

    for (ItemInfo itemDTO : info.items()) {
      Product product = productMap.get(itemDTO.productName());
      BigDecimal price = null;
      if (itemDTO.acceptance()) {
        BigDecimal priceUnit = priceMap.get(product);
        if (priceUnit == null) {
          throw new NoActivePriceFoundException("Нет цены для " + product.getName());
        }
        price = itemDTO.quantity().multiply(priceUnit);
      }

      itemsToSave.add(
          new DeliveryItem(product, record, itemDTO.quantity(), itemDTO.acceptance(), price));

      deliveryItemRepository.saveAll(itemsToSave);
    }
  }

  @Transactional
  public List<DeliveryRecord> getDeliveryRecords() {
    return deliveryRecordRepository.findAll();
  }
}
