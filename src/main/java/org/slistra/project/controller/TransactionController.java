package org.slistra.project.controller;

import org.slistra.project.model.TransactionSummary;
import org.slistra.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/data")
    public Flux<TransactionSummary> getTransactionSummaries() {
        return transactionService.getTransactionSummaries();
    }
}
