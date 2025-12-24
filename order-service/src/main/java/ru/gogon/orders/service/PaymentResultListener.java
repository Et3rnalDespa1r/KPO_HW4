package ru.gogon.orders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogon.orders.model.Order;
import ru.gogon.orders.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class PaymentResultListener {
    private final OrderRepository orderRepo;
    private final SimpMessagingTemplate wsTemplate;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "payment-results", groupId = "orders-group")
    @Transactional
    @SneakyThrows
    public void handlePaymentResult(String message) {
        var node = mapper.readTree(message);
        Long orderId = node.get("orderId").asLong();
        boolean success = node.get("success").asBoolean();

        Order order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus(success ? "FINISHED" : "CANCELLED");
        orderRepo.save(order);

        // Отправка Push в WebSocket
        String jsonPush = "{\"status\":\"" + order.getStatus() + "\"}";
        wsTemplate.convertAndSend("/topic/orders/" + orderId, jsonPush);
    }
}