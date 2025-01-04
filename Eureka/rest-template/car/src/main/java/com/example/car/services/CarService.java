package com.example.car.services;

import com.example.car.entities.Car;
import com.example.car.entities.Client;
import com.example.car.models.CarResponse;
import com.example.car.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String URL = "http://localhost:8888/SERVICE-CLIENT";

    // Retrieve all cars and map to CarResponse
    public List<CarResponse> findAll() {
        List<Car> cars = carRepository.findAll();
        ResponseEntity<Client[]> response = restTemplate.getForEntity(this.URL + "/api/client", Client[].class);
        Client[] clients = response.getBody();
        return cars.stream().map((Car car) -> mapToCarResponse(car, clients)).toList();
    }

    // Retrieve a car by ID and map to CarResponse
    public CarResponse findById(Long id) throws Exception {
        Car car = carRepository.findById(id).orElseThrow(() -> new Exception("Invalid Car Id"));
        Client client = restTemplate.getForObject(this.URL + "/api/client/" + car.getId_client(), Client.class);
        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getMarque())
                .client(client)
                .matricue(car.getMatricule())
                .model(car.getModel())
                .build();
    }

    // Save a new car
    public Car enregistrerCar(Car car) {
        return carRepository.save(car);
    }

    // Update an existing car
    public Car modifierCar(Long id, Car car) {
        return carRepository.findById(id).map(existingCar -> {
            if (car.getMatricule() != null && !car.getMatricule().isEmpty()) {
                existingCar.setMatricule(car.getMatricule());
            }
            if (car.getMarque() != null && !car.getMarque().isEmpty()) {
                existingCar.setMarque(car.getMarque());
            }
            if (car.getModel() != null && !car.getModel().isEmpty()) {
                existingCar.setModel(car.getModel());
            }
            if (car.getId_client() != null) {
                existingCar.setId_client(car.getId_client());
            }
            return carRepository.save(existingCar);
        }).orElseThrow(() -> new RuntimeException("Car not found with ID: " + id));
    }

    // Retrieve all cars without mapping (if needed)
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    // Retrieve a car by ID without mapping (if needed)
    public Car trouverCarParId(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with ID: " + id));
    }

    // Helper function to map Car to CarResponse
    private CarResponse mapToCarResponse(Car car, Client[] clients) {
        Client foundClient = Arrays.stream(clients)
                .filter(client -> client.getId().equals(car.getId_client()))
                .findFirst()
                .orElse(null);

        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getMarque())
                .client(foundClient)
                .matricue(car.getMatricule())
                .model(car.getModel())
                .build();
    }
}