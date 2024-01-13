package com.banco.santander.controllers;

import com.banco.santander.dtos.client.CustomerCreateDTO;
import com.banco.santander.dtos.client.CustomerDTO;
import com.banco.santander.dtos.client.CustomerPatchDTO;
import com.banco.santander.dtos.client.CustomerUpdateDTO;
import com.banco.santander.dtos.payment.PaymentCreateDTO;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalArgumentException;
import com.banco.santander.services.CustomerService;
import com.banco.santander.services.PaymentService;
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
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody @Valid final PaymentCreateDTO paymentCreateDTO)
            throws EntityNotFoundException, IllegalArgumentException {
        paymentService.save(paymentCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Payment success");
    }

    @PatchMapping("/{id}/reverse")
    public ResponseEntity<Object> reversePayment(
            @PathVariable(value = "id") final UUID id
    )
            throws EntityNotFoundException, IllegalArgumentException {
        paymentService.reversePayment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Payment reversed");
    }

    @PatchMapping("/{id}/annulled")
    public ResponseEntity<Object> annulledPayment(
            @PathVariable(value = "id") final UUID id
    )
            throws EntityNotFoundException, IllegalArgumentException {
        paymentService.annulledPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Payment annulled");
    }
}
