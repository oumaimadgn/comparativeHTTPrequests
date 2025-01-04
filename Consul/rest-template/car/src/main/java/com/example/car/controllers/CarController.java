package com.example.car.controllers;

import com.example.car.entities.Client;
import com.example.car.services.ClientService;
import com.example.car.entities.Car;
import com.example.car.repositories.CarRepository;
import com.example.car.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarController {

    @Autowired
    CarRepository voitureRepository;

    @Autowired
    CarService voitureService;

    @Autowired
    ClientService clientService;

    @GetMapping(value = "/voitures", produces = "application/json")
    public ResponseEntity<?> findAll() {
        try {
            List<Car> voitures = voitureRepository.findAll();
            return ResponseEntity.ok(voitures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures: " + e.getMessage());
        }
    }

    @GetMapping("/voitures/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Car voiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture introuvable"));

            // Fetch the client details using the clientService
            voiture.setClient(clientService.clientById(voiture.getId_client()));

            return ResponseEntity.ok(voiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }
    }

    @PostMapping("/voitures/{clientId}")
    public ResponseEntity<?> save(@PathVariable Long clientId, @RequestBody Car voiture) {
        try {
            // Fetch the client details using the clientService
            Client client = clientService.clientById(clientId);

            if (client != null) {
                voiture.setId_client(clientId);
                voiture.setClient(client);
                voitureService.enregistrerCar(voiture);
                return ResponseEntity.ok(voiture);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + clientId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }

    @PutMapping("/voitures/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Car updatedVoiture) {
        try {
            Car existingVoiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: " + id));

            // Update only the non-null fields from the request body
            if (updatedVoiture.getMatricule() != null && !updatedVoiture.getMatricule().isEmpty()) {
                existingVoiture.setMatricule(updatedVoiture.getMatricule());
            }
            if (updatedVoiture.getMarque() != null && !updatedVoiture.getMarque().isEmpty()) {
                existingVoiture.setMarque(updatedVoiture.getMarque());
            }
            if (updatedVoiture.getModel() != null && !updatedVoiture.getModel().isEmpty()) {
                existingVoiture.setModel(updatedVoiture.getModel());
            }

            Car savedVoiture = voitureRepository.save(existingVoiture);
            return ResponseEntity.ok(savedVoiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating voiture: " + e.getMessage());
        }
    }
}
