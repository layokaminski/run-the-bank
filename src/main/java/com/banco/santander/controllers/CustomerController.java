package com.banco.santander.controllers;

import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.dtos.client.CustomerUpdateDTO;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.services.CustomerService;
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
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> save(@RequestBody @Valid final CustomerCreateDTO customerCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(customerCreateDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(
            @PathVariable(value = "id") final UUID id) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findById(id));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CustomerDTO>> getPagedFiltered(
            final SpecificationTemplate.CustomerSpec spec,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) final Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(customerService.getPagedFiltered(spec, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(
            @PathVariable(value = "id") final UUID id,
            @RequestBody @Valid final CustomerUpdateDTO customerUpdateDTO) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.update(id, customerUpdateDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDTO> updatePatch(
            @PathVariable(value = "id") final UUID id,
            @RequestBody @Valid final CustomerPatchDTO customerUpdateDTO) throws EntityNotFoundException {
        final var userUpdated = customerService.updatePatch(id, customerUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
            @PathVariable(value = "id") final UUID id) throws EntityNotFoundException {
        customerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
