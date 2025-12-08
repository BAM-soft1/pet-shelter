package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.BreedDocument;
import org.pet.backendpetshelter.Mongo.Repository.BreedMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class BreedMongoService {

    private final BreedMongoRepository breedRepository;

    public BreedMongoService(BreedMongoRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public List<BreedDocument> getAllBreeds() {
        return breedRepository.findAll();
    }

    public BreedDocument getBreedById(String id) {
        return breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
    }

    public BreedDocument addBreed(BreedDocument breed) {
        if (breed.getId() == null) {
            breed.setId(UUID.randomUUID().toString());
        }
        return breedRepository.save(breed);
    }

    public BreedDocument updateBreed(String id, BreedDocument request) {
        BreedDocument breed = breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        breed.setName(request.getName());
        breed.setSpeciesId(request.getSpeciesId());
        return breedRepository.save(breed);
    }

    public void deleteBreed(String id) {
        if (!breedRepository.existsById(id)) {
            throw new RuntimeException("Breed not found with id: " + id);
        }
        breedRepository.deleteById(id);
    }
}
