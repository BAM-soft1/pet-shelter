package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.SpeciesDocument;
import org.pet.backendpetshelter.Mongo.Repository.SpeciesMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class SpeciesMongoService {

    private final SpeciesMongoRepository speciesRepository;

    public SpeciesMongoService(SpeciesMongoRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    public List<SpeciesDocument> getAllSpecies() {
        return speciesRepository.findAll();
    }

    public SpeciesDocument getSpeciesById(String id) {
        return speciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Species not found with id: " + id));
    }

    public SpeciesDocument addSpecies(SpeciesDocument species) {
        if (species.getId() == null) {
            species.setId(UUID.randomUUID().toString());
        }
        return speciesRepository.save(species);
    }

    public SpeciesDocument updateSpecies(String id, SpeciesDocument request) {
        SpeciesDocument species = speciesRepository.findById(id)
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
