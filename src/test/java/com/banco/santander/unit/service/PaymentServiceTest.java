package com.banco.santander.unit.service;

import com.banco.santander.clients.NotificationClient;
import com.banco.santander.dtos.payment.PaymentCreateDTO;
import com.banco.santander.entities.Payment;
import com.banco.santander.enums.PaymentStatus;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalArgumentException;
import com.banco.santander.repositories.PaymentRepository;
import com.banco.santander.resolver.payment.PaymentCreateDTOResolver;
import com.banco.santander.resolver.payment.PaymentResolver;
import com.banco.santander.services.AccountService;
import com.banco.santander.services.PaymentService;
import com.banco.santander.services.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(PaymentResolver.class)
@ExtendWith(PaymentCreateDTOResolver.class)
public class PaymentServiceTest {
    private PaymentService sut;
    @Mock
    private AccountService accountService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private NotificationClient notificationClient;


    @BeforeEach
    void setup() {
        this.sut = new PaymentServiceImpl(accountService, paymentRepository, notificationClient);
    }

    @Test
    @DisplayName("Should save payment success")
    void should_save_payment_success(PaymentCreateDTO paymentCreateDTO) throws EntityNotFoundException, IllegalArgumentException {
        final var paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        this.sut.save(paymentCreateDTO);

        verify(paymentRepository).save(any(Payment.class));
        verify(paymentRepository).save(paymentCaptor.capture());

        final var capturedPayment = paymentCaptor.getValue();
        assertEquals(paymentCreateDTO.amount(), capturedPayment.getAmount());
        assertEquals(paymentCreateDTO.agencyOrigin(), capturedPayment.getAgencyOrigin());
        assertEquals(paymentCreateDTO.agencyDestination(), capturedPayment.getAgencyDestination());

        verify(notificationClient).sendMessage();
    }

    @Test
    @DisplayName("Should save payment reverse")
    void should_save_payment_reverse(Payment payment) throws EntityNotFoundException, IllegalArgumentException {
        final var paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
        this.sut.reversePayment(payment.getId());

        verify(paymentRepository).save(any(Payment.class));
        verify(paymentRepository).save(paymentCaptor.capture());

        final var capturedPayment = paymentCaptor.getValue();
        assertEquals(payment.getAmount(), capturedPayment.getAmount());
        assertEquals(payment.getAgencyOrigin(), capturedPayment.getAgencyOrigin());
        assertEquals(payment.getAgencyDestination(), capturedPayment.getAgencyDestination());
        assertEquals(PaymentStatus.REVERSED, capturedPayment.getStatus());
    }

    @Test
    @DisplayName("Should save payment annulled")
    void should_save_payment_annulled(Payment payment) throws EntityNotFoundException, IllegalArgumentException {
        final var paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
        this.sut.annulledPayment(payment.getId());

        verify(paymentRepository).save(any(Payment.class));
        verify(paymentRepository).save(paymentCaptor.capture());

        final var capturedPayment = paymentCaptor.getValue();
        assertEquals(payment.getAmount(), capturedPayment.getAmount());
        assertEquals(payment.getAgencyOrigin(), capturedPayment.getAgencyOrigin());
        assertEquals(payment.getAgencyDestination(), capturedPayment.getAgencyDestination());
        assertEquals(PaymentStatus.ANNULLED, capturedPayment.getStatus());
    }

    @Test
    @DisplayName("Should not found payment in method annulled")
    void should_not_found_payment_in_method_annulled() {
        final var id = UUID.randomUUID();

        when(this.paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.sut.annulledPayment(id));
    }

    @Test
    @DisplayName("Should not found payment in method reverse")
    void should_not_found_payment_in_method_reverse() {
        final var id = UUID.randomUUID();

        when(this.paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.sut.reversePayment(id));
    }
}
