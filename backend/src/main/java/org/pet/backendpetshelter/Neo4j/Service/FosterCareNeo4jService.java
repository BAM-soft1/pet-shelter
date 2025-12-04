package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.FosterCareNode;
import org.pet.backendpetshelter.Neo4j.Repository.FosterCareNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class FosterCareNeo4jService {

    private final FosterCareNeo4jRepository fosterCareRepository;

    public FosterCareNeo4jService(FosterCareNeo4jRepository fosterCareRepository) {
        this.fosterCareRepository = fosterCareRepository;
    }

    public List<FosterCareNode> getAllFosterCares() {
        return fosterCareRepository.findAll();
    }

    public FosterCareNode getFosterCareById(String id) {
        return fosterCareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foster Care not found with id: " + id));
    }

    public FosterCareNode addFosterCare(FosterCareNode fosterCare) {
        if (fosterCare.getId() == null) {
            fosterCare.setId(UUID.randomUUID().toString());
        }
        return fosterCareRepository.save(fosterCare);
    }

    public FosterCareNode updateFosterCare(String id, FosterCareNode request) {
        FosterCareNode fosterCare = fosterCareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foster Care not found with id: " + id));
        
        fosterCare.setStartDate(request.getStartDate());
        fosterCare.setEndDate(request.getEndDate());
        fosterCare.setIsActive(request.getIsActive());
        fosterCare.setAnimal(request.getAnimal());
        fosterCare.setFosterParent(request.getFosterParent());
        
        return fosterCareRepository.save(fosterCare);
    }

    public void deleteFosterCare(String id) {
        if (!fosterCareRepository.existsById(id)) {
            throw new RuntimeException("Foster Care not found with id: " + id);
        }
        fosterCareRepository.deleteById(id);
    }
}
