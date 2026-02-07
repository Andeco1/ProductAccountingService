package ru.supplyservice.productAccounting.core.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class Supplier {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID uuid;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  String address;
}
