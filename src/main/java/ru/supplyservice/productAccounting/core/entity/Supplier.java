package ru.supplyservice.productAccounting.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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
