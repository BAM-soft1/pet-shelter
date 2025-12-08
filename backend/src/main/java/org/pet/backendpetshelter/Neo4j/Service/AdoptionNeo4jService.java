package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.AdoptionNode;
import org.pet.backendpetshelter.Neo4j.Repository.AdoptionNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class AdoptionNeo4jService {

    private final AdoptionNeo4jRepository adoptionRepository;

    public AdoptionNeo4jService(AdoptionNeo4jRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }

    public List<AdoptionNode> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    public AdoptionNode getAdoptionById(String id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
    }

    public AdoptionNode addAdoption(AdoptionNode adoption) {
        if (adoption.getId() == null) {
            adoption.setId(UUID.randomUUID().toString());
        }
        adoption.setIsActive(true);
        return adoptionRepository.save(adoption);
    }

    public AdoptionNode updateAdoption(String id, AdoptionNode request) {
        AdoptionNode adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
        
        adoption.setAdoptionDate(request.getAdoptionDate());
        adoption.setIsActive(request.getIsActive());
        adoption.setApplication(request.getApplication());
        
        return adoptionRepository.save(adoption);
    }

    public void deleteAdoption(String id) {
        if (!adoptionRepository.existsById(id)) {
            throw new RuntimeException("Adoption not found with id: " + id);
        }
        adoptionRepository.deleteById(id);
    }
}
