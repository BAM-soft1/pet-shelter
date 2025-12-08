package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.AnimalDocument;
import org.pet.backendpetshelter.Mongo.Repository.AnimalMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class AnimalMongoService {

    private final AnimalMongoRepository animalRepository;

    public AnimalMongoService(AnimalMongoRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<AnimalDocument> getAllAnimals() {
        return animalRepository.findAll();
    }

    public AnimalDocument getAnimalById(String id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));
    }

    public AnimalDocument addAnimal(AnimalDocument animal) {
        if (animal.getId() == null) {
            animal.setId(UUID.randomUUID().toString());
        }
        animal.setIsActive(isStatusActive(animal.getStatus()));
        return animalRepository.save(animal);
    }

    public AnimalDocument updateAnimal(String id, AnimalDocument request) {
        AnimalDocument animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));
        
        animal.setName(request.getName());
        animal.setSex(request.getSex());
        animal.setSpeciesId(request.getSpeciesId());
        animal.setBreedId(request.getBreedId());
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
