package com.banco.santander.dtos.account;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountDTO(
        UUID id,
        String agency,
        Double balance,
        String status,
        String createdAt,
        String updatedAt,
        UUID customerId
) {
}
