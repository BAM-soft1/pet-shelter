package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.AnimalNode;
import org.pet.backendpetshelter.Neo4j.Repository.AnimalNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class AnimalNeo4jService {

    private final AnimalNeo4jRepository animalRepository;

    public AnimalNeo4jService(AnimalNeo4jRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<AnimalNode> getAllAnimals() {
        return animalRepository.findAll();
    }

    public AnimalNode getAnimalById(String id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));
    }

    public AnimalNode addAnimal(AnimalNode animal) {
        if (animal.getId() == null) {
            animal.setId(UUID.randomUUID().toString());
        }
        // Automatically set isActive based on status
        animal.setIsActive(isStatusActive(animal.getStatus()));
        return animalRepository.save(animal);
    }

    public AnimalNode updateAnimal(String id, AnimalNode request) {
        AnimalNode animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));
        
        animal.setName(request.getName());
        animal.setSex(request.getSex());
        animal.setSpecies(request.getSpecies());
        animal.setBreed(request.getBreed());
        animal.setBirthDate(request.getBirthDate());
        animal.setIntakeDate(request.getIntakeDate());
        animal.setStatus(request.getStatus());
        animal.setPrice(request.getPrice());
        animal.setIsActive(isStatusActive(request.getStatus()));
        animal.setImageUrl(request.getImageUrl());
        
        return animalRepository.save(animal);
    }

    public void deleteAnimal(String id) {
        if (!animalRepository.existsById(id)) {
            throw new RuntimeException("Animal not found with id: " + id);
        }
        animalRepository.deleteById(id);
    }

    private boolean isStatusActive(String status) {
        return "available".equalsIgnoreCase(status) || "fostered".equalsIgnoreCase(status);
    }
}
