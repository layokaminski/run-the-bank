package com.banco.santander.services.impl;

import com.banco.santander.dtos.account.AccountBalanceDTO;
import com.banco.santander.dtos.account.AccountCreateDTO;
import com.banco.santander.dtos.account.AccountDTO;
import com.banco.santander.entities.Account;
import com.banco.santander.enums.AccountStatus;
import com.banco.santander.exceptions.customs.AccessDeniedException;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalStateException;
import com.banco.santander.mapper.AccountMapper;
import com.banco.santander.repositories.AccountRepository;
import com.banco.santander.services.AccountService;
import com.banco.santander.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountMapper accountMapper;

    @Override
    public Account save(final AccountCreateDTO accountCreateDTO) throws EntityNotFoundException {
        final var customer = customerService.findCustomerEntityById(accountCreateDTO.customerId());
        final var account = new Account();

        BeanUtils.copyProperties(accountCreateDTO, account);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);

        return accountRepository.save(account);
    }

    @Override
    public AccountDTO findById(final UUID id) throws EntityNotFoundException {
        return accountMapper.accountToCustomerDTO(findAccountEntityById(id));
    }

    @Override
    public Page<AccountDTO> getPagedFiltered(final Specification<Account> spec, final Pageable pageable) {
        return accountRepository.findAll(spec, pageable).map(accountMapper::accountToCustomerDTO);
    }

    @Transactional
    @Override
    public void updatePatch(final UUID id, final UUID currentCustomerId) throws EntityNotFoundException, AccessDeniedException {
        final var account = findAccountEntityById(id);
        final var accountDTO = accountMapper.accountToCustomerDTO(account);

        if (!accountDTO.customerId().equals(currentCustomerId)) {
            throw new AccessDeniedException("Access denied");
        }

        account.setStatus(AccountStatus.INACTIVE);
        accountRepository.save(account);
    }

    @Override
    public void updateDeposit(UUID id, AccountBalanceDTO accountBalanceDTO) throws EntityNotFoundException, IllegalStateException {
        final var account = findAccountEntityById(id);
        final var accountDTO = accountMapper.accountToCustomerDTO(account);

        if (accountDTO.status().equals(AccountStatus.INACTIVE.toString())) {
            throw new IllegalStateException("Account INACTIVE");
        }

        account.setBalance(account.getBalance() + accountBalanceDTO.balance());
        accountRepository.save(account);
    }

    public Account findAccountEntityById(final UUID id) throws EntityNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Override
    public boolean fieldValueExists(final String value, final String fieldName) throws UnsupportedOperationException {
        if (!fieldName.equals("agency")) {
            throw new UnsupportedOperationException("The field is not required");
        }

        return accountRepository.existsByFieldAndValue(value);
    }
}
