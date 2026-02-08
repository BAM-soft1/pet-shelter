package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.AdoptionApplicationDocument;
import org.pet.backendpetshelter.Mongo.Repository.AdoptionApplicationMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class AdoptionApplicationMongoService {

    private final AdoptionApplicationMongoRepository applicationRepository;

    public AdoptionApplicationMongoService(AdoptionApplicationMongoRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<AdoptionApplicationDocument> getAllApplications() {
        return applicationRepository.findAll();
    }

    public AdoptionApplicationDocument getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption Application not found with id: " + id));
    }

    public AdoptionApplicationDocument createApplication(AdoptionApplicationDocument application) {
        if (application.getId() == null) {
            application.setId(UUID.randomUUID().toString());
        }
        return applicationRepository.save(application);
    }

    public AdoptionApplicationDocument updateApplication(String id, AdoptionApplicationDocument request) {
        AdoptionApplicationDocument existing = getApplicationById(id);
        existing.setApplicant(request.getApplicant());
        existing.setAnimal(request.getAnimal());
        existing.setApplicationDate(request.getApplicationDate());
        existing.setDescription(request.getDescription());
        existing.setStatus(request.getStatus());
        existing.setActive(request.isActive());
        existing.setReviewedBy(request.getReviewedBy());
        return applicationRepository.save(existing);
    }

    public void deleteApplication(String id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Adoption Application not found with id: " + id);
        }
        applicationRepository.deleteById(id);
    }
}