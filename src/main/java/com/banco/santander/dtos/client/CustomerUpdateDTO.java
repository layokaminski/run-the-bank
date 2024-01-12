package com.banco.santander.dtos.client;

import com.banco.santander.annotations.Document;
import com.banco.santander.annotations.PasswordValid;
import com.banco.santander.annotations.Unique;
import com.banco.santander.services.CustomerService;

public record CustomerUpdateDTO(
    String name,
    @Document
    String document,
    String address
) { }
