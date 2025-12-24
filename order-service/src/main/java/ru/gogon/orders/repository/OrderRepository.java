package ru.gogon.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogon.orders.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}