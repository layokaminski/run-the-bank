package com.banco.santander.dtos.client;

import java.util.UUID;

public record CustomerDTO(
    UUID id,
    String name,
    String document,
    String address
) { }
