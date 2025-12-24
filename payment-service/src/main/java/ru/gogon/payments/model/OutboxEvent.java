package ru.gogon.payments.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "outbox_events")
@Data @NoArgsConstructor
public class OutboxEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;
    private String payload;
    private boolean processed;
}