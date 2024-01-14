package com.banco.santander.resolver.account;

import com.banco.santander.dtos.account.AccountDTO;
import com.banco.santander.enums.AccountStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountDTOResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == AccountDTO.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new AccountDTO(UUID.randomUUID(), "1234", 100.0, AccountStatus.ACTIVE.toString(), LocalDateTime.now().toString(), LocalDateTime.now().toString(), UUID.randomUUID());
    }
}
