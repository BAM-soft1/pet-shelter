package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.FosterCareDocument;
import org.pet.backendpetshelter.Mongo.Repository.FosterCareMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class FosterCareMongoService {

    private final FosterCareMongoRepository fosterCareRepository;

    public FosterCareMongoService(FosterCareMongoRepository fosterCareRepository) {
        this.fosterCareRepository = fosterCareRepository;
    }

    public List<FosterCareDocument> getAllFosterCares() {
        return fosterCareRepository.findAll();
    }

    public FosterCareDocument getFosterCareById(String id) {
        return fosterCareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foster Care not found with id: " + id));
    }

    public FosterCareDocument addFosterCare(FosterCareDocument fosterCare) {
        if (fosterCare.getId() == null) {
            fosterCare.setId(UUID.randomUUID().toString());
        }
        return fosterCareRepository.save(fosterCare);
    }

    public FosterCareDocument updateFosterCare(String id, FosterCareDocument request) {
        FosterCareDocument fosterCare = fosterCareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foster Care not found with id: " + id));
        
        fosterCare.setStartDate(request.getStartDate());
        fosterCare.setEndDate(request.getEndDate());
        fosterCare.setIsActive(request.getIsActive());
        fosterCare.setAnimalId(request.getAnimalId());
        fosterCare.setFosterParentUserId(request.getFosterParentUserId());
        
        return fosterCareRepository.save(fosterCare);
    }

    public void deleteFosterCare(String id) {
        if (!fosterCareRepository.existsById(id)) {
            throw new RuntimeException("Foster Care not found with id: " + id);
        }
        fosterCareRepository.deleteById(id);
    }
}
