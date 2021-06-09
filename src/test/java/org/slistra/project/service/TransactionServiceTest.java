package org.slistra.project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slistra.project.Fixtures;
import org.slistra.project.model.Transaction;
import org.slistra.project.model.TransactionDirection;
import org.slistra.project.model.TransactionSummary;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private ExternalWebService externalWebService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testTransactionsFromMultipleMonthsGetSeparateSummaries() {
        Transaction transaction1 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 15, 10, 20));
        Transaction transaction2 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.FEBRUARY, 15, 10, 20));

        when(externalWebService.getTransactions()).thenReturn(Flux.just(transaction1, transaction2));

        Flux<TransactionSummary> summaries = transactionService.getTransactionSummaries();
        List<TransactionSummary> transactionSummaries = summaries.collectList().block();

        assertEquals(2, transactionSummaries.size());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).count());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.FEBRUARY.toString())).count());
    }

    @Test
    void testTotalTransactionCountIsCorrectlyCalculated() {
        Transaction transaction1 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 15, 10, 20));
        Transaction transaction2 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 16, 10, 20));
        Transaction transaction3 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.FEBRUARY, 16, 10, 20));

        when(externalWebService.getTransactions()).thenReturn(Flux.just(transaction1, transaction2, transaction3));

        Flux<TransactionSummary> summaries = transactionService.getTransactionSummaries();
        List<TransactionSummary> transactionSummaries = summaries.collectList().block();

        assertEquals(2, transactionSummaries.size());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).count());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.FEBRUARY.toString())).count());
        assertEquals(2, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).findFirst().get().getTotalTransactionCount());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.FEBRUARY.toString())).findFirst().get().getTotalTransactionCount());
    }

    @Test
    void testDebitTransactionCountIsCorrectlyCalculated() {
        Transaction transaction1 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 15, 10, 20), TransactionDirection.DEBIT);
        Transaction transaction2 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 16, 10, 20), TransactionDirection.CREDIT);

        when(externalWebService.getTransactions()).thenReturn(Flux.just(transaction1, transaction2));

        Flux<TransactionSummary> summaries = transactionService.getTransactionSummaries();
        List<TransactionSummary> transactionSummaries = summaries.collectList().block();

        assertEquals(1, transactionSummaries.size());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).count());
        assertEquals(2, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).findFirst().get().getTotalTransactionCount());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).findFirst().get().getDebitTransactionCount());
    }

    @Test
    void testTotalDebitTransactionValueIsCorrectlyCalculated() {
        Transaction transaction1 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 15, 10, 20), TransactionDirection.DEBIT, 2000D);
        Transaction transaction2 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 16, 10, 20), TransactionDirection.DEBIT, 1000D);

        when(externalWebService.getTransactions()).thenReturn(Flux.just(transaction1, transaction2));

        Flux<TransactionSummary> summaries = transactionService.getTransactionSummaries();
        List<TransactionSummary> transactionSummaries = summaries.collectList().block();

        assertEquals(1, transactionSummaries.size());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).count());
        assertEquals(3000D, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).findFirst().get().getTotalDebitTransactionValue());
    }

    @Test
    void testAverageDebitTransactionValueIsCorrectlyCalculated() {
        Transaction transaction1 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 15, 10, 20), TransactionDirection.DEBIT, 2000D);
        Transaction transaction2 = Fixtures.buildTransaction(LocalDateTime.of(2021, Month.JANUARY, 16, 10, 20), TransactionDirection.DEBIT, 1000D);

        when(externalWebService.getTransactions()).thenReturn(Flux.just(transaction1, transaction2));

        Flux<TransactionSummary> summaries = transactionService.getTransactionSummaries();
        List<TransactionSummary> transactionSummaries = summaries.collectList().block();

        assertEquals(1, transactionSummaries.size());
        assertEquals(1, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).count());
        assertEquals(1500D, transactionSummaries.stream().filter(transactionSummary -> transactionSummary.getMonth().equals(Month.JANUARY.toString())).findFirst().get().getAverageDebitTransactionValue());
    }
}
