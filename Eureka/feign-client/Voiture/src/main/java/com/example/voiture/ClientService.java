package com.example.voiture;

import com.example.voiture.entities.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Client")
public interface ClientService {

    @GetMapping("/client/{id}")
    Client clientById(@PathVariable Long id);
}