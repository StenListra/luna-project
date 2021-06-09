package org.slistra.project.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slistra.project.service.TransactionService;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void testServiceIsCalledByController() {
        transactionController.getTransactionSummaries();

        verify(transactionService).getTransactionSummaries();
    }
}
