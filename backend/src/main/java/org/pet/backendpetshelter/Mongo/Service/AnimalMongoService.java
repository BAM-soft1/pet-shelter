package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.AnimalDocument;
import org.pet.backendpetshelter.Mongo.Repository.AnimalMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("mongo")
public class AnimalMongoService {

    private final AnimalMongoRepository animalMongoRepository;

    public AnimalMongoService(AnimalMongoRepository animalMongoRepository) {
        this.animalMongoRepository = animalMongoRepository;
    }

    public List<AnimalDocument> getAllAnimals() {
        return animalMongoRepository.findAll();
    }

    public AnimalDocument getAnimalById(String id) {
        return animalMongoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));
    }

    public AnimalDocument createAnimal(AnimalDocument animal) {
        return animalMongoRepository.save(animal);
    }

    public AnimalDocument updateAnimal(String id, AnimalDocument animal) {
        AnimalDocument existing = getAnimalById(id);
        existing.setName(animal.getName());
        existing.setSex(animal.getSex());
        existing.setBirthDate(animal.getBirthDate());
        existing.setIntakeDate(animal.getIntakeDate());
        existing.setStatus(animal.getStatus());
        existing.setPrice(animal.getPrice());
        existing.setActive(animal.isActive());
        existing.setImageUrl(animal.getImageUrl());
        existing.setSpecies(animal.getSpecies());
        existing.setBreed(animal.getBreed());
        existing.setVaccinations(animal.getVaccinations());
        existing.setMedicalRecords(animal.getMedicalRecords());
        existing.setAdoptionApplications(animal.getAdoptionApplications());
        return animalMongoRepository.save(existing);
    }

    public void deleteAnimal(String id) {
        animalMongoRepository.deleteById(id);
    }
}