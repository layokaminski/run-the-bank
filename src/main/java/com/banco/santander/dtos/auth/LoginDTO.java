package com.banco.santander.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    private String document;
    @NotBlank
    private String password;

}