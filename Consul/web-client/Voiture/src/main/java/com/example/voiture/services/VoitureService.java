package com.example.voiture.services;

import com.example.voiture.entities.Voiture;
import com.example.voiture.repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public VoitureService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Voiture enregistrerVoiture(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    public Voiture modifierVoiture(Long id, Voiture voiture) {
        return voitureRepository.findById(id).map(existingVoiture -> {
            if (voiture.getMatricule() != null && !voiture.getMatricule().isEmpty()) {
                existingVoiture.setMatricule(voiture.getMatricule());
            }
            if (voiture.getMarque() != null && !voiture.getMarque().isEmpty()) {
                existingVoiture.setMarque(voiture.getMarque());
            }
            if (voiture.getModel() != null && !voiture.getModel().isEmpty()) {
                existingVoiture.setModel(voiture.getModel());
            }
            return voitureRepository.save(existingVoiture);
        }).orElseThrow(() -> new RuntimeException("Voiture not found with ID: " + id));
    }

    public List<Voiture> trouverToutesLesVoitures() {
        return voitureRepository.findAll();
    }

    public Voiture trouverVoitureParId(Long id) {
        return voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture not found with ID: " + id));
    }

    public List<Voiture> recupererVoituresExterne() {
        return webClientBuilder.build()
                .get()
                .uri("http://SERVICE-CLIENT/api/client")
                .retrieve()
                .bodyToFlux(Voiture.class)
                .collectList()
                .block();
    }
}
