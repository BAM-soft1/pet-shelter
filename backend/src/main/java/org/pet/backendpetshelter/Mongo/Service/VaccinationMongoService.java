package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.VaccinationDocument;
import org.pet.backendpetshelter.Mongo.Repository.VaccinationMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class VaccinationMongoService {

    private final VaccinationMongoRepository vaccinationRepository;

    public VaccinationMongoService(VaccinationMongoRepository vaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
    }

    public List<VaccinationDocument> getAllVaccinations() {
        return vaccinationRepository.findAll();
    }

    public VaccinationDocument getVaccinationById(String id) {
        return vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
    }

    public VaccinationDocument addVaccination(VaccinationDocument vaccination) {
        if (vaccination.getId() == null) {
            vaccination.setId(UUID.randomUUID().toString());
        }
        return vaccinationRepository.save(vaccination);
    }

    public VaccinationDocument updateVaccination(String id, VaccinationDocument request) {
        VaccinationDocument vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
        
        vaccination.setDateAdministered(request.getDateAdministered());
        vaccination.setNextDueDate(request.getNextDueDate());
        vaccination.setAnimalId(request.getAnimalId());
        vaccination.setVeterinarianId(request.getVeterinarianId());
        vaccination.setVaccinationTypeId(request.getVaccinationTypeId());
        
        return vaccinationRepository.save(vaccination);
    }

    public void deleteVaccination(String id) {
        if (!vaccinationRepository.existsById(id)) {
            throw new RuntimeException("Vaccination not found with id: " + id);
        }
        vaccinationRepository.deleteById(id);
    }
}
