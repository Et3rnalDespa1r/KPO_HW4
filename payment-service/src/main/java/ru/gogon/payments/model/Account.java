package ru.gogon.payments.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data @NoArgsConstructor @AllArgsConstructor
public class Account {
    @Id
    private Long userId;
    private BigDecimal balance;

    @Version
    private Long version;
}