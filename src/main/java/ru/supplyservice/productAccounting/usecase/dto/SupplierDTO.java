package ru.supplyservice.productAccounting.usecase.dto;

import java.util.UUID;

public record SupplierDTO(UUID uuid, String name, String address) {}
