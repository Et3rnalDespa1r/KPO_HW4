package ru.gogon.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogon.orders.model.OutboxEvent;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByProcessedFalse();
}