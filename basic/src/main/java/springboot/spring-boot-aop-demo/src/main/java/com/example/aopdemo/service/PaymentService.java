package com.example.aopdemo.service;

import com.example.aopdemo.annotation.TrackExecution;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @TrackExecution(operation = "PROCESS_PAYMENT")
    public String processPayment(String customerName, double amount) {

        System.out.println("Executing payment business logic");

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        return "Payment of " + amount
                + " processed for " + customerName;
    }
}
