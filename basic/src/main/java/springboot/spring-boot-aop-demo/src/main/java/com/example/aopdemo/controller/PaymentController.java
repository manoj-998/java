package com.example.aopdemo.controller;

import com.example.aopdemo.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /*
     curl command
     http://localhost:8080/payments?amount=1000&customer=Manoj
     */
    @GetMapping("/payments")
    public String processPayment(
            @RequestParam String customer,
            @RequestParam double amount) {

        return paymentService.processPayment(customer, amount);
    }
}
