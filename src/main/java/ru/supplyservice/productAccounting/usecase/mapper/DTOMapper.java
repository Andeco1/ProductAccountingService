package ru.supplyservice.productAccounting.usecase.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.supplyservice.productAccounting.core.entity.DeliveryRecord;
import ru.supplyservice.productAccounting.core.entity.Product;
import ru.supplyservice.productAccounting.core.entity.ProductPricePeriod;
import ru.supplyservice.productAccounting.core.entity.Supplier;
import ru.supplyservice.productAccounting.usecase.dto.DeliveryRecordDTO;
import ru.supplyservice.productAccounting.usecase.dto.ProductDTO;
import ru.supplyservice.productAccounting.usecase.dto.ProductPricePeriodDTO;
import ru.supplyservice.productAccounting.usecase.dto.SupplierDTO;

@Mapper(componentModel = "spring")
public interface DTOMapper {
    ProductDTO productToProductDTO(Product product);
    Product productDTOToProduct(ProductDTO productDTO);
    SupplierDTO supplierToSupplierDTO(Supplier supplier);
    Supplier supplierDTOToSupplier(SupplierDTO supplierDTO);
    @Mapping(target = "supplier", source = "supplierDTO")
    DeliveryRecord deliveryRecordDTOtoDeliveryREcord(DeliveryRecordDTO deliveryRecordDTO);
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "productName", source = "product.name")
    ProductPricePeriodDTO productPricePeriodToProductPricePeriodDTO(ProductPricePeriod productPricePeriod);
}
