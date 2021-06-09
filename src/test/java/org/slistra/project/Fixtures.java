package org.slistra.project;

import org.slistra.project.model.Transaction;
import org.slistra.project.model.TransactionDirection;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class Fixtures {
    private final static Random RANDOM = new Random();

    public static Transaction buildTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(RANDOM.nextDouble() * 100);
        transaction.setBalance(RANDOM.nextDouble() * 1000);
        transaction.setCategory("Payment");
        transaction.setCurrency("EUR");
        transaction.setDirection(TransactionDirection.DEBIT);
        transaction.setDate(LocalDateTime.now());
        transaction.setReference("Reference");

        return transaction;
    }

    public static Transaction buildTransaction(LocalDateTime dateTime) {
        Transaction transaction = buildTransaction();
        transaction.setDate(dateTime);

        return transaction;
    }

    public static Transaction buildTransaction(LocalDateTime dateTime, TransactionDirection transactionDirection) {
        Transaction transaction = buildTransaction();
        transaction.setDate(dateTime);
        transaction.setDirection(transactionDirection);

        return transaction;
    }

    public static Transaction buildTransaction(LocalDateTime dateTime, TransactionDirection transactionDirection, Double amount) {
        Transaction transaction = buildTransaction();
        transaction.setDate(dateTime);
        transaction.setDirection(transactionDirection);
        transaction.setAmount(amount);
        
        return transaction;
    }
}
