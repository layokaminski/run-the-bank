package com.banco.santander.dtos.client;

import com.banco.santander.annotations.Document;
import com.banco.santander.annotations.PasswordValid;
import com.banco.santander.annotations.Unique;
import com.banco.santander.services.CustomerService;

public record CustomerCreateDTO(
    String name,
    @PasswordValid
    String password,
    @Document
    @Unique(service = CustomerService.class, fieldName = "document", message = "Document must be unique")
    String document,
    String address

) { }
