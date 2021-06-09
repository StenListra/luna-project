package org.slistra.project.controller;

import org.junit.jupiter.api.Test;
import org.slistra.project.model.TransactionSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWebTestClient
public class TransactionControllerIT {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetTransactionSummariesReturnsSummaries() {
        List<TransactionSummary> summaries = webTestClient.get()
                .uri("/data")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(List.class)
                .returnResult()
                .getResponseBody();

        assertEquals(12, summaries.size());
    }
}
