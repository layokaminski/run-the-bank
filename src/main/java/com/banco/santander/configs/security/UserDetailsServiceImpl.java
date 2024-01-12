package com.banco.santander.configs.security;

import com.banco.santander.entities.Customer;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.repositories.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;

    public UserDetailsServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UserDetails loadCustomerByDocument(String document) throws EntityNotFoundException {
        Customer customer = customerRepository.findByDocument(document)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return UserDetailsImpl.build(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var customer = customerRepository.findByDocument(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        return UserDetailsImpl.build(customer);
    }
}
