package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.AdoptionDocument;
import org.pet.backendpetshelter.Mongo.Repository.AdoptionMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class AdoptionMongoService {

    private final AdoptionMongoRepository adoptionRepository;

    public AdoptionMongoService(AdoptionMongoRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }

    public List<AdoptionDocument> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    public AdoptionDocument getAdoptionById(String id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
    }

    public AdoptionDocument addAdoption(AdoptionDocument adoption) {
        if (adoption.getId() == null) {
            adoption.setId(UUID.randomUUID().toString());
        }
        adoption.setIsActive(true);
        return adoptionRepository.save(adoption);
    }

    public AdoptionDocument updateAdoption(String id, AdoptionDocument request) {
        AdoptionDocument adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
        
        adoption.setAdoptionDate(request.getAdoptionDate());
        adoption.setIsActive(request.getIsActive());
        adoption.setApplicationId(request.getApplicationId());
        
        return adoptionRepository.save(adoption);
    }

    public void deleteAdoption(String id) {
        if (!adoptionRepository.existsById(id)) {
            throw new RuntimeException("Adoption not found with id: " + id);
        }
        adoptionRepository.deleteById(id);
    }
}
