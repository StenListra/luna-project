package org.slistra.project.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Transaction {
    private UUID id;
    private String currency;
    private Double amount;
    private String category;
    private Double balance;
    private LocalDateTime date;
    private TransactionDirection direction;
    private String reference;
}
