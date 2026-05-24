package com.perfectmarket.modules.payment.service;


import com.perfectmarket.modules.payment.domain.StoredPaymentMethod;
import com.perfectmarket.modules.payment.dto.command.AddPaymentMethodCommand;
import com.perfectmarket.modules.payment.repository.PaymentStoredMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;@RequiredArgsConstructor
@Service
public class PaymentMethodManagementService {
    private final PaymentStoredMethodRepository paymentStoredMethodRepository;
    public void addStoredPaymentMethod(UUID userId, AddPaymentMethodCommand command) {
        paymentStoredMethodRepository.findByProviderAndExternalToken(command.provider(), command.externalToken())
                .ifPresent(existing -> {
                    throw new RuntimeException("Payment method already exists");
                });

        StoredPaymentMethod newMethod = new StoredPaymentMethod(
                userId,
                command.provider(),
                command.externalToken()
        );

        boolean hasAnyMethod = paymentStoredMethodRepository.existsByUserId(userId);
        if (command.makeDefault() || !hasAnyMethod) {
            handleSetDefault(userId, newMethod);
        }

        paymentStoredMethodRepository.save(newMethod);
    }

    public void deactivatePaymentMethod(Long userId, UUID methodId) {
        StoredPaymentMethod method = paymentStoredMethodRepository.findById(methodId)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        if (!method.getUserId().equals(userId)) {
            throw new RuntimeException("NOT_ENOUGH_PERMISSION");
        }

        method.deactivate();
        paymentStoredMethodRepository.save(method);
    }

    public void setDefaultPaymentMethod(UUID userId, UUID methodId) {
        StoredPaymentMethod newDefault = paymentStoredMethodRepository.findById(methodId)
                .filter(m -> m.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        handleSetDefault(userId, newDefault);
        paymentStoredMethodRepository.save(newDefault);
    }

    public void deleteStoredPaymentMethod(UUID userId, UUID paymentMethodId) {
        var paymentMethod = paymentStoredMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        if (!paymentMethod.getUserId().equals(userId)) {
            throw new RuntimeException("NOT_ENOUGH_PERMISSION");
        }

        paymentStoredMethodRepository.delete(paymentMethod);
    }

    private void handleSetDefault(UUID userId, StoredPaymentMethod newDefault) {
        paymentStoredMethodRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(StoredPaymentMethod::unsetDefault);

        newDefault.setDefault();
    }
}