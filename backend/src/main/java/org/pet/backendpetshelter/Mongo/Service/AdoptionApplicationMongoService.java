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

    public AdoptionApplicationDocument addApplication(AdoptionApplicationDocument application) {
        if (application.getId() == null) {
            application.setId(UUID.randomUUID().toString());
        }
        return applicationRepository.save(application);
    }

    public AdoptionApplicationDocument updateApplication(String id, AdoptionApplicationDocument request) {
        AdoptionApplicationDocument application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption Application not found with id: " + id));

        // Embedded objekter — ikke userId/animalId
        application.setUser(request.getUser());
        application.setAnimal(request.getAnimal());
        application.setApplicationDate(request.getApplicationDate());
        application.setDescription(request.getDescription());
        application.setStatus(request.getStatus());
        application.setActive(request.isActive());
        application.setReviewedBy(request.getReviewedBy());

        return applicationRepository.save(application);
    }

    public void deleteApplication(String id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Adoption Application not found with id: " + id);
        }
        applicationRepository.deleteById(id);
    }
}