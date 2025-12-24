package ru.gogon.payments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gogon.payments.model.Account;
import ru.gogon.payments.service.PaymentService;
import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final PaymentService service;

    @PostMapping
    public Account deposit(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        return service.deposit(userId, amount);
    }
}