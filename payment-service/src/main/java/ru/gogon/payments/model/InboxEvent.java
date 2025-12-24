package ru.gogon.payments.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inbox_events")
@Data @NoArgsConstructor @AllArgsConstructor
public class InboxEvent {
    @Id
    private String messageId;
}