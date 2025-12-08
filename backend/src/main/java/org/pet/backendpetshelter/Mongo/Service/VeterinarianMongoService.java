package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.VeterinarianDocument;
import org.pet.backendpetshelter.Mongo.Repository.VeterinarianMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class VeterinarianMongoService {

    private final VeterinarianMongoRepository veterinarianRepository;

    public VeterinarianMongoService(VeterinarianMongoRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
    }

    public List<VeterinarianDocument> getAllVeterinarians() {
        return veterinarianRepository.findAll();
    }

    public VeterinarianDocument getVeterinarianById(String id) {
        return veterinarianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinarian not found with id: " + id));
    }

    public VeterinarianDocument addVeterinarian(VeterinarianDocument veterinarian) {
        if (veterinarian.getId() == null) {
            veterinarian.setId(UUID.randomUUID().toString());
        }
        veterinarian.setIsActive(true);
        return veterinarianRepository.save(veterinarian);
    }

    public VeterinarianDocument updateVeterinarian(String id, VeterinarianDocument request) {
        VeterinarianDocument veterinarian = veterinarianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinarian not found with id: " + id));
        
        veterinarian.setLicenseNumber(request.getLicenseNumber());
        veterinarian.setClinicName(request.getClinicName());
        veterinarian.setIsActive(request.getIsActive());
        veterinarian.setUserId(request.getUserId());
        
        return veterinarianRepository.save(veterinarian);
    }

    public void deleteVeterinarian(String id) {
        if (!veterinarianRepository.existsById(id)) {
            throw new RuntimeException("Veterinarian not found with id: " + id);
        }
        veterinarianRepository.deleteById(id);
    }
}
