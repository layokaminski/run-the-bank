package com.banco.santander.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO (
        @NotBlank
        String document,
        @NotBlank
        String password
) {}