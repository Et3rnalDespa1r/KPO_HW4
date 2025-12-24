package ru.gogon.payments.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.gogon.payments.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {}