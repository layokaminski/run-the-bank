package com.banco.santander.services.impl;

import com.banco.santander.dtos.account.AccountCreateDTO;
import com.banco.santander.dtos.account.AccountDTO;
import com.banco.santander.entities.Account;
import com.banco.santander.enums.AccountStatus;
import com.banco.santander.exceptions.customs.AccessDeniedException;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalArgumentException;
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
        return accountMapper.accountToAccountDTO(findAccountEntityById(id));
    }

    @Override
    public Page<AccountDTO> getPagedFiltered(final Specification<Account> spec, final Pageable pageable) {
        return accountRepository.findAll(spec, pageable).map(accountMapper::accountToAccountDTO);
    }

    @Transactional
    @Override
    public void updatePatch(final UUID id, final UUID currentCustomerId) throws EntityNotFoundException, AccessDeniedException {
        final var account = findAccountEntityById(id);
        final var accountDTO = accountMapper.accountToAccountDTO(account);

        if (!accountDTO.customerId().equals(currentCustomerId)) {
            throw new AccessDeniedException("Access denied");
        }

        account.setStatus(AccountStatus.INACTIVE);
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void updateDeposit(String agencyDestination, Double amount) throws EntityNotFoundException {
        final var account = findAccountEntityByAgency(agencyDestination);

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Transactional
    @Override
    public void updateTransfer(String agencyOrigin, Double amount) throws EntityNotFoundException, IllegalArgumentException {
        final var account = findAccountEntityByAgency(agencyOrigin);
        if (amount > account.getBalance() || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0 or greater balance");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    public Account findAccountEntityByAgency(final String agency) throws EntityNotFoundException {
        return accountRepository.findByAgency(agency)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
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
