package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.VaccineTypeSpeciesNode;
import org.pet.backendpetshelter.Neo4j.Repository.VaccineTypeSpeciesNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class VaccineTypeSpeciesNeo4jService {

    private final VaccineTypeSpeciesNeo4jRepository vaccineTypeSpeciesRepository;

    public VaccineTypeSpeciesNeo4jService(VaccineTypeSpeciesNeo4jRepository vaccineTypeSpeciesRepository) {
        this.vaccineTypeSpeciesRepository = vaccineTypeSpeciesRepository;
    }

    public List<VaccineTypeSpeciesNode> getAllVaccineTypeSpecies() {
        return vaccineTypeSpeciesRepository.findAll();
    }

    public VaccineTypeSpeciesNode getVaccineTypeSpeciesById(String id) {
        return vaccineTypeSpeciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));
    }

    public VaccineTypeSpeciesNode addVaccineTypeSpecies(VaccineTypeSpeciesNode vaccineTypeSpecies) {
        if (vaccineTypeSpecies.getId() == null) {
            vaccineTypeSpecies.setId(UUID.randomUUID().toString());
        }
        return vaccineTypeSpeciesRepository.save(vaccineTypeSpecies);
    }

    public VaccineTypeSpeciesNode updateVaccineTypeSpecies(String id, VaccineTypeSpeciesNode request) {
        VaccineTypeSpeciesNode vaccineTypeSpecies = vaccineTypeSpeciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccineTypeSpecies not found with id: " + id));
        
        vaccineTypeSpecies.setSpecies(request.getSpecies());
        vaccineTypeSpecies.setVaccinationType(request.getVaccinationType());
        
        return vaccineTypeSpeciesRepository.save(vaccineTypeSpecies);
    }

    public void deleteVaccineTypeSpecies(String id) {
        if (!vaccineTypeSpeciesRepository.existsById(id)) {
            throw new RuntimeException("VaccineTypeSpecies not found with id: " + id);
        }
        vaccineTypeSpeciesRepository.deleteById(id);
    }
}
