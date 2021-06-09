package org.slistra.project.service;

import org.slistra.project.model.TransactionDirection;
import org.slistra.project.model.TransactionSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TransactionService {
    private final ExternalWebService externalWebService;

    @Autowired
    public TransactionService(ExternalWebService externalWebService) {
        this.externalWebService = externalWebService;
    }

    public Flux<TransactionSummary> getTransactionSummaries() {
        return externalWebService.getTransactions()
                .groupBy(transaction -> transaction.getDate().getMonth())
                .flatMap(value -> value.collectList().map(transactions -> {
                    TransactionSummary transactionSummary = new TransactionSummary();
                    transactionSummary.setMonth(value.key().name());
                    transactionSummary.setTotalTransactionCount(transactions.size());
                    transactions.forEach(transaction -> {
                        if (transaction.getDirection().equals(TransactionDirection.DEBIT)) {
                            transactionSummary.setDebitTransactionCount(transactionSummary.getDebitTransactionCount() + 1);
                            transactionSummary.setTotalDebitTransactionValue(transactionSummary.getTotalDebitTransactionValue() + transaction.getAmount());
                        }
                    });
                    transactionSummary.setAverageDebitTransactionValue(transactionSummary.getTotalDebitTransactionValue() / transactionSummary.getDebitTransactionCount());
                    return transactionSummary;
                }));
    }
}
