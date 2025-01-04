package com.example.voiture.services;

import com.example.voiture.entities.Client;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ClientService {

    private final WebClient webClient;

    public ClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://CLIENT-SERVICE").build();
    }

    public Client clientById(Long id) {
        return webClient.get()
                .uri("/client/{id}", id)
                .retrieve()
                .bodyToMono(Client.class)
                .block();
    }
}
