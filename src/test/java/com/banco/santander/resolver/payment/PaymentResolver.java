package com.banco.santander.resolver.payment;

import com.banco.santander.entities.Payment;
import com.banco.santander.enums.PaymentStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.UUID;

public class PaymentResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Payment.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final var payment = new Payment();

        payment.setId(UUID.randomUUID());
        payment.setAgencyOrigin("1234");
        payment.setAgencyDestination("4321");
        payment.setAmount(100.0);
        payment.setStatus(PaymentStatus.SUCCESS);

        return payment;
    }
}
