package com.example.voiture;

import com.example.voiture.entities.Client;
import com.example.voiture.entities.Voiture;
import com.example.voiture.repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class VoitureApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoitureApplication.class, args);
	}

	@Autowired
	private ClientService clientService;

	@Bean
	CommandLineRunner initialiserBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
		return args -> {
			Client c1 = clientService.clientById(2L);
			Client c2 = clientService.clientById(1L);

			System.out.println("***************************");
			System.out.println("Id est :" + c2.getId());
			System.out.println("Nom est :" + c2.getNom());
			System.out.println("***************************");

			System.out.println("***************************");
			System.out.println("Id est :" + c1.getId());
			System.out.println("Nom est :" + c1.getNom());
			System.out.println("Age est :" + c1.getAge());
			System.out.println("***************************");

			voitureRepository.save(new Voiture(null, "A 25 333", "Toyota", "Corolla", c2));
			voitureRepository.save(new Voiture(null, "B 6 3456", "Renault", "Megane", c2));
			voitureRepository.save(new Voiture(null, "A 55 4444", "Peugeot", "301", c1));
		};
	}


	@Bean
	CommandLineRunner testClientById() {
		return args -> {
			Long clientId = 1L;
			Client client = clientService.clientById(clientId);
			System.out.println("Client retrieved: " + client);
		};
	}
}
