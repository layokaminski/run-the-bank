package com.banco.santander.services.impl;

import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.dtos.client.CustomerUpdateDTO;
import com.banco.santander.entities.Customer;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.mapper.CustomerMapper;
import com.banco.santander.repositories.CustomerRepository;
import com.banco.santander.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerServiceImpl  implements CustomerService {
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public CustomerDTO save(CustomerCreateDTO customerCreateDTO) {
        Customer customer = new Customer();

        BeanUtils.copyProperties(customerCreateDTO, customer);
        final var encryptedPassword = passwordEncoder.encode(customer.getPassword());

        customer.setPassword(encryptedPassword);

        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO findById(UUID id) throws EntityNotFoundException {
        return customerMapper.customerToCustomerDTO(findCustomerEntityById(id));
    }

    @Override
    public Page<CustomerDTO> getPagedFiltered(final Specification<Customer> spec, final Pageable pageable) {
        return customerRepository.findAll(spec,pageable).map(customerMapper::customerToCustomerDTO);
    }

    @Transactional
    @Override
    public CustomerDTO update(UUID id, CustomerUpdateDTO customerUpdateDTO) throws EntityNotFoundException {
        final var customer = findCustomerEntityById(id);
        BeanUtils.copyProperties(customerUpdateDTO, customer);

        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @Transactional
    @Override
    public CustomerDTO updatePatch(UUID id, CustomerPatchDTO customerUpdateDTO) throws EntityNotFoundException {
        final var customer = findCustomerEntityById(id);

        customerMapper.customerPatchDTOFromCustomer(customerUpdateDTO, customer);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @Transactional
    @Override
    public void delete(UUID id) throws EntityNotFoundException {
        final var customer = findCustomerEntityById(id);
        customerRepository.delete(customer);
    }

    private Customer findCustomerEntityById(final UUID id) throws EntityNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public boolean fieldValueExists(String value, String fieldName) throws UnsupportedOperationException {
        if (!fieldName.equals("document")) {
            throw new UnsupportedOperationException("The field is not required");
        }

        return customerRepository.existsByFieldAndValue(value);
    }
}
