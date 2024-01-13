package com.banco.santander.controllers;

import com.banco.santander.configs.security.AuthenticationCurrentCustomerService;
import com.banco.santander.dtos.account.AccountBalanceDTO;
import com.banco.santander.dtos.account.AccountCreateDTO;
import com.banco.santander.dtos.account.AccountDTO;
import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.entities.Account;
import com.banco.santander.exceptions.customs.AccessDeniedException;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalStateException;
import com.banco.santander.services.AccountService;
import com.banco.santander.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    final private AccountService accountService;
    final private AuthenticationCurrentCustomerService authenticationCurrentCustomerService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid final AccountCreateDTO accountCreateDTO) throws EntityNotFoundException {
        Account account = accountService.save(accountCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created for: " + account.getCustomer().getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findById(
            @PathVariable(value = "id") final UUID id) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.findById(id));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<AccountDTO>> getPagedFiltered(
            final SpecificationTemplate.AccountSpec spec,
            @RequestParam(required = false) UUID customerId,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) final Pageable pageable) {

        if (customerId != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(accountService.getPagedFiltered(
                            SpecificationTemplate.filterAccountByCustomerId(customerId), pageable));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                accountService.getPagedFiltered(spec, pageable));
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Object> updatePatch(
            @PathVariable(value = "id") final UUID id) throws EntityNotFoundException, AccessDeniedException {
        UUID currentCustomerId = authenticationCurrentCustomerService.getCurrentCustomer().getId();
        accountService.updatePatch(id, currentCustomerId);

        return ResponseEntity.status(HttpStatus.OK).body("Account change status INACTIVE");
    }

    @PatchMapping("/{id}/deposit")
    public ResponseEntity<Object> updateDeposit(
            @PathVariable(value = "id") final UUID id,
            @RequestBody @Valid final AccountBalanceDTO accountBalanceDTO
    ) throws EntityNotFoundException, IllegalStateException {
        accountService.updateDeposit(id, accountBalanceDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Deposit success");
    }
}
