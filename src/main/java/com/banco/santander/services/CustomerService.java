package com.banco.santander.services;

import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.dtos.client.CustomerUpdateDTO;
import com.banco.santander.entities.Customer;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CustomerService extends FieldValueExists {
    CustomerDTO save(final CustomerCreateDTO customerCreateDTO);
    CustomerDTO findById(UUID id) throws EntityNotFoundException;
    Page<CustomerDTO> getPagedFiltered(final Specification<Customer> spec, final Pageable pageable);
    CustomerDTO update(UUID id, CustomerUpdateDTO customerUpdateDTO) throws EntityNotFoundException;

    CustomerDTO updatePatch(UUID id, CustomerPatchDTO customerUpdateDTO) throws EntityNotFoundException;
    void delete(UUID id) throws EntityNotFoundException;
}
