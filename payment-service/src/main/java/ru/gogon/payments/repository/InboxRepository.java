package ru.gogon.payments.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogon.payments.model.InboxEvent;

public interface InboxRepository extends JpaRepository<InboxEvent, String> {}