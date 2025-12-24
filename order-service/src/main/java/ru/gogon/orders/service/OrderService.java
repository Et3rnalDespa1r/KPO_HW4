package ru.gogon.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogon.orders.model.Order;
import ru.gogon.orders.model.OutboxEvent;
import ru.gogon.orders.repository.OrderRepository;
import ru.gogon.orders.repository.OutboxRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final OutboxRepository outboxRepo;

    @Transactional
    public Order createOrder(Long userId, BigDecimal amount) {
        // 1. Сохраняем заказ
        Order order = new Order(null, userId, amount, "NEW");
        order = orderRepo.save(order);

        // 2. Transactional Outbox: пишем событие в ту же транзакцию БД
        OutboxEvent event = new OutboxEvent();
        event.setTopic("payment-requests");
        event.setProcessed(false);
        event.setPayload("{\"orderId\":" + order.getId() + ",\"userId\":" + userId + ",\"amount\":" + amount + "}");
        outboxRepo.save(event);

        return order;
    }

    public Order getOrder(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}