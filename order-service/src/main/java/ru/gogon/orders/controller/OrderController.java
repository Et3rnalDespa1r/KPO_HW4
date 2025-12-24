package ru.gogon.orders.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gogon.orders.model.Order;
import ru.gogon.orders.service.OrderService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService service;

    @PostMapping
    public Order create(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        return service.createOrder(userId, amount);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return service.getOrder(id);
    }
}