package com.banco.santander.services.impl;

import com.banco.santander.clients.NotificationClient;
import com.banco.santander.dtos.payment.PaymentCreateDTO;
import com.banco.santander.entities.Payment;
import com.banco.santander.enums.PaymentStatus;
import com.banco.santander.exceptions.customs.EntityNotFoundException;
import com.banco.santander.exceptions.customs.IllegalArgumentException;
import com.banco.santander.repositories.PaymentRepository;
import com.banco.santander.services.AccountService;
import com.banco.santander.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountService accountService;
    private final PaymentRepository paymentRepository;
    private final NotificationClient notificationClient;

    @Transactional
    @Override
    public void save(PaymentCreateDTO paymentCreateDTO) throws EntityNotFoundException, IllegalArgumentException {
        final var payment = new Payment();

        accountService.updateTransfer(paymentCreateDTO.agencyOrigin(), paymentCreateDTO.amount());
        accountService.updateDeposit(paymentCreateDTO.agencyDestination(), paymentCreateDTO.amount());

        BeanUtils.copyProperties(paymentCreateDTO, payment);
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        notificationClient.sendMessage();
    }

    @Transactional
    @Override
    public void reversePayment(UUID id) throws EntityNotFoundException, IllegalArgumentException {
        final var payment = findPaymentEntityById(id);

        accountService.updateTransfer(payment.getAgencyDestination(), payment.getAmount());
        accountService.updateDeposit(payment.getAgencyOrigin(), payment.getAmount());

        payment.setStatus(PaymentStatus.REVERSED);
        paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public void annulledPayment(UUID id) throws EntityNotFoundException, IllegalArgumentException {
        final var payment = findPaymentEntityById(id);

        accountService.updateTransfer(payment.getAgencyDestination(), payment.getAmount());
        accountService.updateDeposit(payment.getAgencyOrigin(), payment.getAmount());

        payment.setStatus(PaymentStatus.ANNULLED);
        paymentRepository.save(payment);
    }


    public Payment findPaymentEntityById(final UUID id) throws EntityNotFoundException {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }
}
