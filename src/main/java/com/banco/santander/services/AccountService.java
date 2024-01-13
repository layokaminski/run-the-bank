package com.banco.santander.services;

import com.banco.santander.dtos.account.AccountBalanceDTO;
import com.banco.santander.dtos.account.AccountCreateDTO;
import com.banco.santander.dtos.account.AccountDTO;
import com.banco.santander.entities.Account;
import com.banco.santander.exceptions.customs.AccessDeniedException;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalArgumentException;
import com.banco.santander.exceptions.customs.IllegalStateException;
import com.banco.santander.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface AccountService extends FieldValueExists {
    Account save(final AccountCreateDTO accountCreateDTO) throws EntityNotFoundException;
    AccountDTO findById(final UUID id) throws EntityNotFoundException;
    Page<AccountDTO> getPagedFiltered(final Specification<Account> spec, final Pageable pageable);
    void updatePatch(UUID id, UUID currentCustomerId) throws EntityNotFoundException, AccessDeniedException;
    void updateDeposit(final String agencyDestination, final Double amount) throws EntityNotFoundException;
    void updateTransfer(final String agencyOrigin, final Double amount) throws EntityNotFoundException, IllegalArgumentException;
}
