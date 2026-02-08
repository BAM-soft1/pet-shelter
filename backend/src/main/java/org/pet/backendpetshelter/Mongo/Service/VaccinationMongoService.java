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

    public List<VaccinationDocument> getAll() {
        return vaccinationRepository.findAll();
    }

    public VaccinationDocument getById(String id) {
        return vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
    }

    public VaccinationDocument create(VaccinationDocument vaccination) {
        if (vaccination.getId() == null) {
            vaccination.setId(UUID.randomUUID().toString());
        }
        return vaccinationRepository.save(vaccination);
    }

    public VaccinationDocument update(String id, VaccinationDocument request) {
        VaccinationDocument existing = getById(id);
        existing.setAnimal(request.getAnimal());
        existing.setVeterinarian(request.getVeterinarian());
        existing.setVaccinationType(request.getVaccinationType());
        existing.setDateAdministered(request.getDateAdministered());
        existing.setNextDueDate(request.getNextDueDate());
        return vaccinationRepository.save(existing);
    }

    public void delete(String id) {
        if (!vaccinationRepository.existsById(id)) {
            throw new RuntimeException("Vaccination not found with id: " + id);
        }
        vaccinationRepository.deleteById(id);
    }
}