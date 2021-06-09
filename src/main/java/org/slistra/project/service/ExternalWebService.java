package org.slistra.project.service;

import org.slistra.project.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

@Service
public class ExternalWebService {
    private final static URI externalUri = URI.create("https://5c937fdb4dca5d0014ad825b.mockapi.io/data");
    private final WebClient webClient;

    @Autowired
    public ExternalWebService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Transaction> getTransactions() {
        return webClient.get()
                .uri(externalUri)
                .retrieve()
                .bodyToFlux(Transaction.class);
    }
}
