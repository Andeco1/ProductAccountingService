package ru.supplyservice.productAccounting.core.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DeliveryRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @ManyToOne
  @JoinColumn(name = "supplier_id", nullable = false)
  Supplier supplier;

  @Column(nullable = false)
  Instant date;

  String info;

  public DeliveryRecord(Supplier supplier, Instant date, String info) {
    this.supplier = supplier;
    this.date = date;
    this.info = info;
  }
}
