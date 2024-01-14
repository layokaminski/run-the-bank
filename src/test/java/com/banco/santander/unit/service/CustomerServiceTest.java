package com.banco.santander.unit.service;

import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.dtos.client.CustomerUpdateDTO;
import com.banco.santander.entities.Customer;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.mapper.CustomerMapper;
import com.banco.santander.repositories.CustomerRepository;
import com.banco.santander.resolver.customer.*;
import com.banco.santander.services.CustomerService;
import com.banco.santander.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(CustomerCreateDTOResolver.class)
@ExtendWith(CustomerResolver.class)
@ExtendWith(CustomerDTOResolver.class)
@ExtendWith(CustomerPatchDTOResolver.class)
@ExtendWith(CustomerUpdateDTOResolver.class)
public class CustomerServiceTest {
    private CustomerService sut;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        this.sut = new CustomerServiceImpl(passwordEncoder, customerRepository, customerMapper);
    }

    @Test
    @DisplayName("Should save new customer")
    void should_save_new_customer(CustomerCreateDTO customerCreateDTO) {
        final var customerCaptor = ArgumentCaptor.forClass(Customer.class);

        this.sut.save(customerCreateDTO);

        verify(customerRepository).save(any(Customer.class));
        verify(customerRepository).save(customerCaptor.capture());

        final var capturedCustomer = customerCaptor.getValue();
        assertEquals(customerCreateDTO.name(), capturedCustomer.getName());
        assertEquals(customerCreateDTO.address(), capturedCustomer.getAddress());
        assertEquals(customerCreateDTO.document(), capturedCustomer.getDocument());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found")
    void should_throw_exception_when_customer_not_found() {
        final var id = UUID.randomUUID();

        when(this.customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.sut.findCustomerEntityById(id));
    }

    @Test
    @DisplayName("Should find customer with success")
    void should_find_customer_by_id(Customer customer) throws EntityNotFoundException {
        final var id = UUID.randomUUID();

        when(this.customerRepository.findById(id)).thenReturn(Optional.of(customer));

        this.sut.findById(id);

        verify(this.customerRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should find all paginated filtered customers")
    void should_find_all_paginated_filtered_customers() {
        Specification<Customer> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = PageRequest.of(0, 10);

        List<Customer> fakeCustomers = new ArrayList<>();

        Page<Customer> fakeCustomerPage = new PageImpl<>(fakeCustomers, pageable, fakeCustomers.size());

        when(customerRepository.findAll(spec, pageable)).thenReturn(fakeCustomerPage);

        Page<CustomerDTO> result = this.sut.getPagedFiltered(spec, pageable);

        verify(customerRepository).findAll(spec, pageable);

        assertEquals(fakeCustomerPage.map(customerMapper::customerToCustomerDTO), result);
    }

    @Test
    @DisplayName("Should delete customer")
    void should_delete_customer(Customer customer) throws EntityNotFoundException {
        final var customerCaptor = ArgumentCaptor.forClass(Customer.class);
        final var id = UUID.randomUUID();

        when(this.customerRepository.findById(id)).thenReturn(Optional.of(customer));

        this.sut.delete(id);

        verify(this.customerRepository, times(1)).findById(id);
        verify(this.customerRepository, times(1)).delete(customerCaptor.capture());

        assertEquals(customer, customerCaptor.getValue());
    }

    @Test
    @DisplayName("Should update partially customer")
    void should_update_partially_customer(
            CustomerPatchDTO customerPatchDTO, Customer customer) throws EntityNotFoundException {
        final var customerCaptor = ArgumentCaptor.forClass(Customer.class);
        final var id = UUID.randomUUID();

        when(this.customerRepository.findById(id)).thenReturn(Optional.of(customer));
        doAnswer(invocationOnMock -> {
            CustomerPatchDTO customerPatchDTOMapper = invocationOnMock.getArgument(0);
            Customer customerMapper = invocationOnMock.getArgument(1);

            customerMapper.setAddress(customerPatchDTOMapper.address());
            return null;
        }).when(customerMapper).customerPatchDTOFromCustomer(customerPatchDTO, customer);

        this.sut.updatePatch(id, customerPatchDTO);

        verify(this.customerRepository, times(1)).findById(id);
        verify(this.customerRepository).save(customerCaptor.capture());

        final var capturedCustomer = customerCaptor.getValue();
        assertEquals(customerPatchDTO.address(), capturedCustomer.getAddress());
        assertEquals(customerPatchDTO.document(), capturedCustomer.getDocument());
    }

    @Test
    @DisplayName("Should update customer")
    void should_update_customer_with_correct_values(CustomerUpdateDTO customerUpdateDTO, Customer customer) throws EntityNotFoundException {
        final var customerCaptor = ArgumentCaptor.forClass(Customer.class);
        final var id = UUID.randomUUID();

        when(this.customerRepository.findById(id)).thenReturn(Optional.of(customer));

        this.sut.update(id, customerUpdateDTO);

        verify(this.customerRepository, times(1)).findById(id);
        verify(this.customerRepository).save(customerCaptor.capture());

        final var customerCap = customerCaptor.getValue();
        assertEquals(customerUpdateDTO.name(), customerCap.getName());
        assertEquals(customerUpdateDTO.document(), customerCap.getDocument());
        assertEquals(customerUpdateDTO.address(), customerCap.getAddress());
    }
}
