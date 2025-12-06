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

    private final VaccineTypeSpeciesMongoRepository vaccineTypeSpeciesRepository;

    public VaccineTypeSpeciesMongoService(VaccineTypeSpeciesMongoRepository vaccineTypeSpeciesRepository) {
        this.vaccineTypeSpeciesRepository = vaccineTypeSpeciesRepository;
    }

    public List<VaccineTypeSpeciesDocument> getAllVaccineTypeSpecies() {
        return vaccineTypeSpeciesRepository.findAll();
    }

    public VaccineTypeSpeciesDocument getVaccineTypeSpeciesById(String id) {
        return vaccineTypeSpeciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));
    }

    public VaccineTypeSpeciesDocument addVaccineTypeSpecies(VaccineTypeSpeciesDocument vaccineTypeSpecies) {
        if (vaccineTypeSpecies.getId() == null) {
            vaccineTypeSpecies.setId(UUID.randomUUID().toString());
        }
        return vaccineTypeSpeciesRepository.save(vaccineTypeSpecies);
    }

    public VaccineTypeSpeciesDocument updateVaccineTypeSpecies(String id, VaccineTypeSpeciesDocument request) {
        VaccineTypeSpeciesDocument vaccineTypeSpecies = vaccineTypeSpeciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));
        
        vaccineTypeSpecies.setSpeciesId(request.getSpeciesId());
        vaccineTypeSpecies.setVaccinationTypeId(request.getVaccinationTypeId());
        
        return vaccineTypeSpeciesRepository.save(vaccineTypeSpecies);
    }

    public void deleteVaccineTypeSpecies(String id) {
        if (!vaccineTypeSpeciesRepository.existsById(id)) {
            throw new RuntimeException("VaccineTypeSpecies not found with id: " + id);
        }
        vaccineTypeSpeciesRepository.deleteById(id);
    }
}
