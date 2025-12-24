package ru.gogon.payments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogon.payments.repository.OutboxRepository;
import ru.gogon.payments.model.OutboxEvent;

@Service
@RequiredArgsConstructor
public class OutboxSender {
    private final OutboxRepository outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void send() {
        var events = outboxRepo.findByProcessedFalse();
        for (var event : events) {
            kafkaTemplate.send(event.getTopic(), event.getPayload());
            event.setProcessed(true);
        }
    }
}