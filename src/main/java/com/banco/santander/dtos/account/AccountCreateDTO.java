package com.banco.santander.dtos.account;

import com.banco.santander.annotations.Unique;
import com.banco.santander.services.AccountService;
import com.banco.santander.services.CustomerService;

import java.util.UUID;

public record AccountCreateDTO(
        @Unique(service = AccountService.class, fieldName = "agency", message = "agency must be unique")
    String agency,
    Double balance,
    UUID customerId
) {
}
