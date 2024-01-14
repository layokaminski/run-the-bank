package com.banco.santander.resolver;

import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.entities.Customer;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.UUID;

public class CustomerResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Customer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final var customer = new Customer();

        customer.setId(UUID.randomUUID());
        customer.setName("Layo");
        customer.setPassword("StrongPassword951!#@");
        customer.setDocument("12345678910");
        customer.setAddress("Cidade de Pallet");
        return customer;
    }
}
