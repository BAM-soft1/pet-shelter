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

    public List<VaccinationTypeDocument> getAll() {
        return vaccinationTypeRepository.findAll();
    }

    public VaccinationTypeDocument getById(String id) {
        return vaccinationTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VaccinationType not found with id: " + id));
    }

    public VaccinationTypeDocument create(VaccinationTypeDocument vaccinationType) {
        if (vaccinationType.getId() == null) {
            vaccinationType.setId(UUID.randomUUID().toString());
        }
        return vaccinationTypeRepository.save(vaccinationType);
    }

    public VaccinationTypeDocument update(String id, VaccinationTypeDocument request) {
        VaccinationTypeDocument existing = getById(id);
        existing.setVaccineName(request.getVaccineName());
        existing.setDescription(request.getDescription());
        existing.setDurationMonths(request.getDurationMonths());
        existing.setRequiredForAdoption(request.isRequiredForAdoption());
        return vaccinationTypeRepository.save(existing);
    }

    public void delete(String id) {
        if (!vaccinationTypeRepository.existsById(id)) {
            throw new RuntimeException("VaccinationType not found with id: " + id);
        }
        vaccinationTypeRepository.deleteById(id);
    }
}