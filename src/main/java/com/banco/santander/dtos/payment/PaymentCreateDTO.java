package com.banco.santander.dtos.payment;

public record PaymentCreateDTO(
    String agencyOrigin,
    String agencyDestination,
    Double amount
) { }
