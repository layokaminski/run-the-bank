package com.banco.santander.unit.service;

import com.banco.santander.dtos.account.AccountCreateDTO;
import com.banco.santander.dtos.account.AccountDTO;
import com.banco.santander.entities.Account;
import com.banco.santander.entities.Customer;
import com.banco.santander.enums.AccountStatus;
import com.banco.santander.exceptions.customs.AccessDeniedException;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.mapper.AccountMapper;
import com.banco.santander.repositories.AccountRepository;
import com.banco.santander.resolver.account.AccountBalanceDTOResolver;
import com.banco.santander.resolver.account.AccountCreateDTOResolver;
import com.banco.santander.resolver.account.AccountDTOResolver;
import com.banco.santander.resolver.account.AccountResolver;
import com.banco.santander.services.AccountService;
import com.banco.santander.services.CustomerService;
import com.banco.santander.services.impl.AccountServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AccountCreateDTOResolver.class)
@ExtendWith(AccountResolver.class)
@ExtendWith(AccountDTOResolver.class)
@ExtendWith(AccountBalanceDTOResolver.class)
public class AccountServiceTest {
    private AccountService sut;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private AccountMapper accountMapper;


    @BeforeEach
    void setup() {
        this.sut = new AccountServiceImpl(accountRepository, customerService, accountMapper);
    }

    @Test
    @DisplayName("Should save new account")
    void should_save_new_account(AccountCreateDTO accountCreateDTO) throws EntityNotFoundException {
        final var accountCaptor = ArgumentCaptor.forClass(Account.class);
        Customer customer = new Customer();
        customer.setId(accountCreateDTO.customerId());

        when(this.customerService.findCustomerEntityById(customer.getId())).thenReturn(customer);

        this.sut.save(accountCreateDTO);

        verify(accountRepository).save(any(Account.class));
        verify(accountRepository).save(accountCaptor.capture());

        final var capturedAccount = accountCaptor.getValue();
        assertEquals(accountCreateDTO.agency(), capturedAccount.getAgency());
        assertEquals(accountCreateDTO.balance(), capturedAccount.getBalance());
    }

    @Test
    @DisplayName("Should not save account not found customer")
    void should_not_save_account_not_found_customer(AccountCreateDTO accountCreateDTO) throws EntityNotFoundException {
        when(this.customerService.findCustomerEntityById(accountCreateDTO.customerId()))
                .thenThrow(new EntityNotFoundException("Customer not found"));

        assertThrows(EntityNotFoundException.class, () -> this.sut.save(accountCreateDTO));

        verify(this.accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find account with success")
    void should_find_account_by_id(Account account) throws EntityNotFoundException {
        final var id = UUID.randomUUID();

        when(this.accountRepository.findById(id)).thenReturn(Optional.of(account));

        this.sut.findById(id);

        verify(this.accountRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should find all paginated filtered accounts")
    void should_find_all_paginated_filtered_accounts() {
        Specification<Account> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = PageRequest.of(0, 10);

        List<Account> fakeAccounts = new ArrayList<>();

        Page<Account> fakeAccountPage = new PageImpl<>(fakeAccounts, pageable, fakeAccounts.size());

        when(this.accountRepository.findAll(spec, pageable)).thenReturn(fakeAccountPage);

        Page<AccountDTO> result = this.sut.getPagedFiltered(spec, pageable);

        verify(accountRepository).findAll(spec, pageable);

        assertEquals(fakeAccountPage.map(accountMapper::accountToAccountDTO), result);
    }

    @Test
    @DisplayName("Should update partially account")
    void should_update_partially_account(Account account, AccountDTO accountDTO) throws EntityNotFoundException, AccessDeniedException {
        final var id = UUID.randomUUID();
        final var currentCustomerId = accountDTO.customerId();

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountMapper.accountToAccountDTO(account)).thenReturn(accountDTO);

        this.sut.updatePatch(id, currentCustomerId);

        verify(accountRepository).save(account);
        assertEquals(AccountStatus.INACTIVE, account.getStatus());
    }

    @Test
    @DisplayName("Should deposit amount")
    void should_deposit_amount(Account account) throws EntityNotFoundException {
        final var amount = 50.0;
        final var lastBalance = account.getBalance();

        when(accountRepository.findByAgency(account.getAgency())).thenReturn(Optional.of(account));

        this.sut.updateDeposit(account.getAgency(), amount);

        verify(accountRepository).save(account);
        assertEquals(account.getBalance() - amount, lastBalance);
    }

    @Test
    @DisplayName("Should transfer amount")
    void should_transfer_amount(Account account) throws EntityNotFoundException {
        final var amount = 50.0;
        final var lastBalance = account.getBalance();

        when(accountRepository.findByAgency(account.getAgency())).thenReturn(Optional.of(account));

        this.sut.updateDeposit(account.getAgency(), amount);

        verify(accountRepository).save(account);
        assertEquals(account.getBalance(), lastBalance + amount);
    }
}
