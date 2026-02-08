package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.VaccineTypeSpeciesDocument;
import org.pet.backendpetshelter.Mongo.Repository.VaccineTypeSpeciesMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class VaccineTypeSpeciesMongoService {

    private final VaccineTypeSpeciesMongoRepository repository;

    public VaccineTypeSpeciesMongoService(VaccineTypeSpeciesMongoRepository repository) {
        this.repository = repository;
    }

    public List<VaccineTypeSpeciesDocument> getAll() {
        return repository.findAll();
    }

    public VaccineTypeSpeciesDocument getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));
    }

    public VaccineTypeSpeciesDocument create(VaccineTypeSpeciesDocument doc) {
        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID().toString());
        }
        return repository.save(doc);
    }

    public VaccineTypeSpeciesDocument update(String id, VaccineTypeSpeciesDocument request) {
        VaccineTypeSpeciesDocument existing = getById(id);
        existing.setSpecies(request.getSpecies());
        existing.setVaccinationType(request.getVaccinationType());
        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("VaccineTypeSpecies not found with id: " + id);
        }
        repository.deleteById(id);
    }
}