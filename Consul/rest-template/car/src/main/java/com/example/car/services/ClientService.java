package com.example.car.services;

import com.example.car.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {

    @Autowired
    private RestTemplate restTemplate;

    private final String CLIENT_SERVICE_URL = "http://localhost:8888/SERVICE-CLIENT";

    // Fetch a client by ID
    public Client clientById(Long id) {
        String url = CLIENT_SERVICE_URL + "/client/" + id;
        return restTemplate.getForObject(url, Client.class);
    }
}
