package ru.supplyservice.productAccounting.core.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class DeliveryItem {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  Product product;

  @ManyToOne
  @JoinColumn(name = "delivery_record_id", nullable = false)
  DeliveryRecord deliveryRecord;

  @Column(nullable = false)
  BigDecimal quantity;

  @Column(nullable = false)
  Boolean acceptance;

  BigDecimal priceOfAccepted;

  public DeliveryItem(
      Product product,
      DeliveryRecord deliveryRecord,
      BigDecimal quantity,
      Boolean acceptance,
      BigDecimal priceOfAccepted) {
    this.product = product;
    this.deliveryRecord = deliveryRecord;
    this.quantity = quantity;
    this.acceptance = acceptance;
    this.priceOfAccepted = priceOfAccepted;
  }
}
