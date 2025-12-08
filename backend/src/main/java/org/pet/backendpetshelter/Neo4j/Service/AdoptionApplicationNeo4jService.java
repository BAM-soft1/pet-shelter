package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.AdoptionApplicationNode;
import org.pet.backendpetshelter.Neo4j.Repository.AdoptionApplicationNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class AdoptionApplicationNeo4jService {

    private final AdoptionApplicationNeo4jRepository applicationRepository;

    public AdoptionApplicationNeo4jService(AdoptionApplicationNeo4jRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<AdoptionApplicationNode> getAllApplications() {
        return applicationRepository.findAll();
    }

    public AdoptionApplicationNode getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption Application not found with id: " + id));
    }

    public AdoptionApplicationNode addApplication(AdoptionApplicationNode application) {
        if (application.getId() == null) {
            application.setId(UUID.randomUUID().toString());
        }
        return applicationRepository.save(application);
    }

    public AdoptionApplicationNode updateApplication(String id, AdoptionApplicationNode request) {
        AdoptionApplicationNode application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption Application not found with id: " + id));
        
        application.setApplicationDate(request.getApplicationDate());
        application.setStatus(request.getStatus());
        application.setIsActive(request.getIsActive());
        application.setUser(request.getUser());
        application.setAnimal(request.getAnimal());
        application.setReviewedByUser(request.getReviewedByUser());
        
        return applicationRepository.save(application);
    }

    public void deleteApplication(String id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Adoption Application not found with id: " + id);
        }
        applicationRepository.deleteById(id);
    }
}
