package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.BreedNode;
import org.pet.backendpetshelter.Neo4j.Repository.BreedNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class BreedNeo4jService {

    private final BreedNeo4jRepository breedRepository;

    public BreedNeo4jService(BreedNeo4jRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public List<BreedNode> getAllBreeds() {
        return breedRepository.findAll();
    }

    public BreedNode getBreedById(String id) {
        return breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
    }

    public BreedNode addBreed(BreedNode breed) {
        if (breed.getId() == null) {
            breed.setId(UUID.randomUUID().toString());
        }
        return breedRepository.save(breed);
    }

    public BreedNode updateBreed(String id, BreedNode request) {
        BreedNode breed = breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        breed.setName(request.getName());
        breed.setSpecies(request.getSpecies());
        return breedRepository.save(breed);
    }

    public void deleteBreed(String id) {
        if (!breedRepository.existsById(id)) {
            throw new RuntimeException("Breed not found with id: " + id);
        }
        breedRepository.deleteById(id);
    }
}
