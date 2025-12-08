package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.VaccinationTypeDocument;
import org.pet.backendpetshelter.Mongo.Repository.VaccinationTypeMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class VaccinationTypeMongoService {

    private final VaccinationTypeMongoRepository vaccinationTypeRepository;

    public VaccinationTypeMongoService(VaccinationTypeMongoRepository vaccinationTypeRepository) {
        this.vaccinationTypeRepository = vaccinationTypeRepository;
    }

    public List<VaccinationTypeDocument> getAllVaccinationTypes() {
        return vaccinationTypeRepository.findAll();
    }

    public VaccinationTypeDocument getVaccinationTypeById(String id) {
        return vaccinationTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination Type not found with id: " + id));
    }

    public VaccinationTypeDocument addVaccinationType(VaccinationTypeDocument vaccinationType) {
        if (vaccinationType.getId() == null) {
            vaccinationType.setId(UUID.randomUUID().toString());
        }
        return vaccinationTypeRepository.save(vaccinationType);
    }

    public VaccinationTypeDocument updateVaccinationType(String id, VaccinationTypeDocument request) {
        VaccinationTypeDocument vaccinationType = vaccinationTypeRepository.findById(id)
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
