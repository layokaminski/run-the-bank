package com.banco.santander.dtos.client;

import com.banco.santander.annotations.Document;
import com.banco.santander.annotations.Unique;
import com.banco.santander.services.CustomerService;
import jakarta.annotation.Nullable;
import lombok.Builder;


public record CustomerPatchDTO(
    String name,
    @Unique(service = CustomerService.class, fieldName = "document", message = "Document must be unique")
    String document,
    String address
) { }
