package org.slistra.project.model;

import lombok.Data;

@Data
public class TransactionSummary {
    private String month;
    private int totalTransactionCount;
    private int debitTransactionCount;
    private double totalDebitTransactionValue;
    private double averageDebitTransactionValue;
}
