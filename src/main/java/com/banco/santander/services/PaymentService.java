package com.banco.santander.services;

import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.dtos.client.CustomerUpdateDTO;
import com.banco.santander.dtos.payment.PaymentCreateDTO;
import com.banco.santander.entities.Customer;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalArgumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface PaymentService {
    void save(final PaymentCreateDTO paymentCreateDTO) throws EntityNotFoundException, IllegalArgumentException;
    void reversePayment(final UUID id) throws EntityNotFoundException, IllegalArgumentException;
    void annulledPayment(final UUID id) throws EntityNotFoundException, IllegalArgumentException;
}
