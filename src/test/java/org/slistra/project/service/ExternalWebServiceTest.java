package org.slistra.project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slistra.project.Fixtures;
import org.slistra.project.model.Transaction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExternalWebServiceTest {
    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ExternalWebService externalWebService;

    @Test
    void testServiceReturnsExpectedFlux() {
        Transaction transaction = Fixtures.buildTransaction();
        when(responseSpec.bodyToFlux(Transaction.class)).thenReturn(Flux.just(transaction));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        Flux<Transaction> transactionFlux = externalWebService.getTransactions();

        verify(webClient).get();
        Assertions.assertEquals(transaction, transactionFlux.blockFirst());
    }
}
