package ru.gogon.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gogon.orders.repository.OutboxRepository;
import ru.gogon.orders.model.OutboxEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxWorker {
    private final OutboxRepository outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processOutbox() {
        List<OutboxEvent> events = outboxRepo.findByProcessedFalse();
        for (OutboxEvent event : events) {
            kafkaTemplate.send(event.getTopic(), event.getPayload());
            event.setProcessed(true);
        }
    }
}