package ru.gogon.payments.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogon.payments.model.OutboxEvent;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByProcessedFalse();
}