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

    public AnimalDocument createAnimal(AnimalDocument animal) {
        if (animal.getId() == null) {
            animal.setId(UUID.randomUUID().toString());
        }
        return animalRepository.save(animal);
    }

    public AnimalDocument updateAnimal(String id, AnimalDocument request) {
        AnimalDocument existing = getAnimalById(id);
        existing.setName(request.getName());
        existing.setSex(request.getSex());
        existing.setBirthDate(request.getBirthDate());
        existing.setIntakeDate(request.getIntakeDate());
        existing.setStatus(request.getStatus());
        existing.setPrice(request.getPrice());
        existing.setActive(request.isActive());
        existing.setImageUrl(request.getImageUrl());
        existing.setSpecies(request.getSpecies());
        existing.setBreed(request.getBreed());
        existing.setVaccinations(request.getVaccinations());
        existing.setMedicalRecords(request.getMedicalRecords());
        existing.setAdoptionApplications(request.getAdoptionApplications());
        return animalRepository.save(existing);
    }

    public void deleteAnimal(String id) {
        if (!animalRepository.existsById(id)) {
            throw new RuntimeException("Animal not found with id: " + id);
        }
        animalRepository.deleteById(id);
    }
}