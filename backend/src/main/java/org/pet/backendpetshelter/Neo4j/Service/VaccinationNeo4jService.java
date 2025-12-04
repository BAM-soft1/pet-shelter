package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.VaccinationNode;
import org.pet.backendpetshelter.Neo4j.Repository.VaccinationNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class VaccinationNeo4jService {

    private final VaccinationNeo4jRepository vaccinationRepository;

    public VaccinationNeo4jService(VaccinationNeo4jRepository vaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
    }

    public List<VaccinationNode> getAllVaccinations() {
        return vaccinationRepository.findAll();
    }

    public VaccinationNode getVaccinationById(String id) {
        return vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
    }

    public VaccinationNode addVaccination(VaccinationNode vaccination) {
        if (vaccination.getId() == null) {
            vaccination.setId(UUID.randomUUID().toString());
        }
        return vaccinationRepository.save(vaccination);
    }

    public VaccinationNode updateVaccination(String id, VaccinationNode request) {
        VaccinationNode vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
        
        vaccination.setDateAdministered(request.getDateAdministered());
        vaccination.setNextDueDate(request.getNextDueDate());
        vaccination.setAnimal(request.getAnimal());
        vaccination.setVeterinarian(request.getVeterinarian());
        vaccination.setVaccinationType(request.getVaccinationType());
        
        return vaccinationRepository.save(vaccination);
    }

    public void deleteVaccination(String id) {
        if (!vaccinationRepository.existsById(id)) {
            throw new RuntimeException("Vaccination not found with id: " + id);
        }
        vaccinationRepository.deleteById(id);
    }
}
