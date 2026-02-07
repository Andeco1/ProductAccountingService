package ru.supplyservice.productAccounting.usecase.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.supplyservice.productAccounting.core.entity.*;
import ru.supplyservice.productAccounting.usecase.dto.*;

@Mapper(componentModel = "spring")
public interface DTOMapper {
  ProductDTO productToProductDTO(Product product);

  Product productDTOToProduct(ProductDTO productDTO);

  SupplierDTO supplierToSupplierDTO(Supplier supplier);

  Supplier supplierDTOToSupplier(SupplierDTO supplierDTO);

  @Mapping(target = "supplier", source = "supplierDTO")
  DeliveryRecord deliveryRecordDTOtoDeliveryRecord(DeliveryRecordDTO deliveryRecordDTO);

  @Mapping(target = "supplierDTO", source = "supplier")
  DeliveryRecordDTO deliveryRecordtoDeliveryRecordDTO(DeliveryRecord deliveryRecord);

  @Mapping(target = "supplierName", source = "supplier.name")
  @Mapping(target = "productName", source = "product.name")
  ProductPricePeriodDTO productPricePeriodToProductPricePeriodDTO(
      ProductPricePeriod productPricePeriod);

  @Mapping(target = "productDTO", source = "product")
  @Mapping(target = "deliveryRecordDTO", source = "deliveryRecord")
  DeliveryItemDTO deliveryItemToDeliveryItemDTO(DeliveryItem deliveryItem);
}
