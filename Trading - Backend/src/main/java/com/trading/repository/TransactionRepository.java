package com.trading.repository;

import com.trading.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Used for GET /transactions/{userId}
    List<Transaction> findByUserIdOrderByTransactionTimeDesc(Long userId);

}
