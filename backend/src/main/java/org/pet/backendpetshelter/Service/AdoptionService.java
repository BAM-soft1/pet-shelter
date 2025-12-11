package org.pet.backendpetshelter.Service;


import org.pet.backendpetshelter.DTO.AdoptionRequest;
import org.pet.backendpetshelter.DTO.AdoptionResponse;
import org.pet.backendpetshelter.Entity.Adoption;
import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Repository.AdoptionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Profile("mysql")
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;

    public AdoptionService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }



    public Page<AdoptionResponse> GetAllAdoptions(Pageable pageable) {
        return adoptionRepository.findAll(pageable)
                .map(AdoptionResponse::new);
    }


    public AdoptionResponse GetAdoptionById(Long id) {
        return adoptionRepository.findById(id)
                .map(AdoptionResponse::new)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
    }

    public AdoptionResponse addAdoption(AdoptionRequest request) {
        validateApplication(request.getAdoptionApplication());
        validateAdoptionDate(request.getAdoptionDate());
        validateIsActive(request.getIsActive());

        Adoption adoption = new Adoption();
        adoption.setApplication(request.getAdoptionApplication());
        adoption.setAdoptionDate(request.getAdoptionDate());
        adoption.setIsActive(true);

        adoptionRepository.save(adoption);
        return new AdoptionResponse(adoption);
    }

    // Validation Methods

    private void validateApplication(AdoptionApplication application) {
        if (application == null || application.getId() == null) {
            throw new IllegalArgumentException("Adoption Application cannot be null");
        }

    }

    private void validateAdoptionDate(java.util.Date adoptionDate) {
        if (adoptionDate == null) {
            throw new IllegalArgumentException("Adoption date cannot be null");
        }

    }

    private void validateIsActive(Boolean isActive) {
        if (isActive == null) {
            throw new IllegalArgumentException("IsActive cannot be null");
        }
    }


    /* Update Adoption */
    public AdoptionResponse updateAdoption(Long id, AdoptionRequest request) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));

        adoption.setApplication(request.getAdoptionApplication());
        adoption.setAdoptionDate(request.getAdoptionDate());
        adoption.setIsActive(request.getIsActive());

        adoptionRepository.save(adoption);
        return new AdoptionResponse(adoption);
    }

    /* Delete Adoption */
    public void deleteAdoption(Long id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
        adoptionRepository.delete(adoption);
    }



    }



