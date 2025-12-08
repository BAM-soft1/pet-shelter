package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.VaccinationTypeNode;
import org.pet.backendpetshelter.Neo4j.Repository.VaccinationTypeNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class VaccinationTypeNeo4jService {

    private final VaccinationTypeNeo4jRepository vaccinationTypeRepository;

    public VaccinationTypeNeo4jService(VaccinationTypeNeo4jRepository vaccinationTypeRepository) {
        this.vaccinationTypeRepository = vaccinationTypeRepository;
    }

    public List<VaccinationTypeNode> getAllVaccinationTypes() {
        return vaccinationTypeRepository.findAll();
    }

    public VaccinationTypeNode getVaccinationTypeById(String id) {
        return vaccinationTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination Type not found with id: " + id));
    }

    public VaccinationTypeNode addVaccinationType(VaccinationTypeNode vaccinationType) {
        if (vaccinationType.getId() == null) {
            vaccinationType.setId(UUID.randomUUID().toString());
        }
        return vaccinationTypeRepository.save(vaccinationType);
    }

    public VaccinationTypeNode updateVaccinationType(String id, VaccinationTypeNode request) {
        VaccinationTypeNode vaccinationType = vaccinationTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination Type not found with id: " + id));
        
        vaccinationType.setVaccineName(request.getVaccineName());
        vaccinationType.setDescription(request.getDescription());
        vaccinationType.setDurationMonths(request.getDurationMonths());
        vaccinationType.setRequiredForAdoption(request.getRequiredForAdoption());
        
        return vaccinationTypeRepository.save(vaccinationType);
    }

    public void deleteVaccinationType(String id) {
        if (!vaccinationTypeRepository.existsById(id)) {
            throw new RuntimeException("Vaccination Type not found with id: " + id);
        }
        vaccinationTypeRepository.deleteById(id);
    }
}
