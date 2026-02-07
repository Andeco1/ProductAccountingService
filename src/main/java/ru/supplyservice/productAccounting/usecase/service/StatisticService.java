package ru.supplyservice.productAccounting.usecase.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.supplyservice.productAccounting.api.response.AcceptedItemInfo;
import ru.supplyservice.productAccounting.api.response.ReportResponse;
import ru.supplyservice.productAccounting.api.response.StatInfo;
import ru.supplyservice.productAccounting.usecase.dto.DeliveryItemDTO;

@Service
public class StatisticService {
  DeliveryItemService deliveryItemService;

  @Autowired
  public StatisticService(DeliveryItemService deliveryItemService) {
    this.deliveryItemService = deliveryItemService;
  }

  public ReportResponse getReportOfPeriod(Instant dateFrom, Instant dateTo) {
    List<DeliveryItemDTO> deliveryItemDTOList =
        deliveryItemService.getAcceptedDeliveryItemsOfPeriod(dateFrom, dateTo);
    BigDecimal quantityOfPeriod = BigDecimal.ZERO;
    BigDecimal priceOfPeriod = BigDecimal.ZERO;
    Map<String, SupplierInfo> acceptedItemInfoMap = new HashMap<>();
    for (DeliveryItemDTO e : deliveryItemDTOList) {
      String supplierName = e.deliveryRecordDTO().supplierDTO().name();
      SupplierInfo supplierInfo =
          acceptedItemInfoMap.getOrDefault(
              e.deliveryRecordDTO().supplierDTO().name(), SupplierInfo.getDefault());
      supplierInfo.acceptedItemInfoList.add(deliveryItemToAcceptedItemInfo(e));
      supplierInfo.addQuantity(e.quantity());
      supplierInfo.addPrice(e.priceOfAccepted());
      quantityOfPeriod = quantityOfPeriod.add(e.quantity());
      priceOfPeriod = priceOfPeriod.add(e.priceOfAccepted());
      acceptedItemInfoMap.put(supplierName, supplierInfo);
    }
    return new ReportResponse(
        quantityOfPeriod,
        priceOfPeriod,
        acceptedItemInfoMap.entrySet().stream()
            .map(
                e ->
                    new StatInfo(
                        e.getKey(),
                        e.getValue().acceptedItemInfoList,
                        e.getValue().totalItemsQuantity,
                        e.getValue().totalItemsPrice))
            .toList());
  }

  @AllArgsConstructor
  private static class SupplierInfo {
    List<AcceptedItemInfo> acceptedItemInfoList;
    BigDecimal totalItemsQuantity;
    BigDecimal totalItemsPrice;

    public void addQuantity(BigDecimal quantity) {
      totalItemsQuantity = totalItemsQuantity.add(quantity);
    }

    public void addPrice(BigDecimal price) {
      totalItemsPrice = totalItemsPrice.add(price);
    }

    public static SupplierInfo getDefault() {
      return new SupplierInfo(new ArrayList<>(), BigDecimal.ZERO, BigDecimal.ZERO);
    }
  }

  private AcceptedItemInfo deliveryItemToAcceptedItemInfo(DeliveryItemDTO deliveryItemDTO) {
    return new AcceptedItemInfo(
        deliveryItemDTO.productDTO().name(),
        deliveryItemDTO.quantity(),
        deliveryItemDTO.priceOfAccepted());
  }
}
