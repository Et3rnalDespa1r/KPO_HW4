package ru.gogon.payments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogon.payments.model.*;
import ru.gogon.payments.repository.*;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final AccountRepository accountRepo;
    private final InboxRepository inboxRepo;
    private final OutboxRepository outboxRepo;
    private final ObjectMapper mapper;

    // --- Обработка входящего запроса на оплату ---
    @KafkaListener(topics = "payment-requests", groupId = "payments-group")
    @Transactional
    @SneakyThrows
    public void processPayment(String msg,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) long offset) {

        // 1. Transactional Inbox: Идемпотентность
        String msgId = partition + "_" + offset;
        if (inboxRepo.existsById(msgId)) return; // Если уже видели это сообщение - игнор
        inboxRepo.save(new InboxEvent(msgId));

        // Парсим JSON
        var node = mapper.readTree(msg);
        Long userId = node.get("userId").asLong();
        BigDecimal amount = new BigDecimal(node.get("amount").asText());
        Long orderId = node.get("orderId").asLong();

        // 2. Логика списания
        boolean success = false;
        Account acc = accountRepo.findById(userId).orElse(null);

        if (acc != null && acc.getBalance().compareTo(amount) >= 0) {
            acc.setBalance(acc.getBalance().subtract(amount));
            accountRepo.save(acc); // Тут сработает Optimistic Lock, если что
            success = true;
        }

        // 3. Transactional Outbox: Готовим ответ
        OutboxEvent res = new OutboxEvent();
        res.setTopic("payment-results");
        res.setProcessed(false);
        res.setPayload("{\"orderId\":" + orderId + ",\"success\":" + success + "}");
        outboxRepo.save(res);
    }

    // Метод для пополнения (вызывается из контроллера)
    @Transactional
    public Account deposit(Long userId, BigDecimal amount) {
        Account acc = accountRepo.findById(userId).orElse(new Account(userId, BigDecimal.ZERO, 0L));
        acc.setBalance(acc.getBalance().add(amount));
        return accountRepo.save(acc);
    }
}