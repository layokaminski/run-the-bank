package com.banco.santander.resolver.account;

import com.banco.santander.entities.Account;
import com.banco.santander.entities.Customer;
import com.banco.santander.enums.AccountStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.UUID;

public class AccountResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Account.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final var account = new Account();

        final var customer = new Customer();

        customer.setId(UUID.randomUUID());
        customer.setName("Layo");
        customer.setPassword("StrongPassword951!#@");
        customer.setDocument("12345678910");
        customer.setAddress("Cidade de Pallet");

        account.setId(UUID.randomUUID());
        account.setAgency("1234");
        account.setBalance(100.0);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);

        return account;
    }
}
