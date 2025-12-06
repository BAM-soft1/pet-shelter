package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.SpeciesNode;
import org.pet.backendpetshelter.Neo4j.Repository.SpeciesNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class SpeciesNeo4jService {

    private final SpeciesNeo4jRepository speciesRepository;

    public SpeciesNeo4jService(SpeciesNeo4jRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    public List<SpeciesNode> getAllSpecies() {
        return speciesRepository.findAll();
    }

    public SpeciesNode getSpeciesById(String id) {
        return speciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Species not found with id: " + id));
    }

    public SpeciesNode addSpecies(SpeciesNode species) {
        if (species.getId() == null) {
            species.setId(UUID.randomUUID().toString());
        }
        return speciesRepository.save(species);
    }

    public SpeciesNode updateSpecies(String id, SpeciesNode request) {
        SpeciesNode species = speciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Species not found with id: " + id));
        species.setName(request.getName());
        return speciesRepository.save(species);
    }

    public void deleteSpecies(String id) {
        if (!speciesRepository.existsById(id)) {
            throw new RuntimeException("Species not found with id: " + id);
        }
        speciesRepository.deleteById(id);
    }
}
