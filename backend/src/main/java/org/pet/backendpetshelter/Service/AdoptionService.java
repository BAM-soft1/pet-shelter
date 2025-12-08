package org.pet.backendpetshelter.Service;

import org.pet.backendpetshelter.DTO.AdoptionRequest;
import org.pet.backendpetshelter.DTO.AdoptionResponse;
import org.pet.backendpetshelter.Entity.Adoption;
import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Repository.AdoptionRepository;
import org.pet.backendpetshelter.Repository.AdoptionApplicationRepository;
import org.pet.backendpetshelter.Repository.AnimalRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.pet.backendpetshelter.Status;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("mysql")
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AdoptionApplicationRepository applicationRepository;
    private final AnimalRepository animalRepository;

    public AdoptionService(
            AdoptionRepository adoptionRepository,
            AdoptionApplicationRepository applicationRepository,
            AnimalRepository animalRepository) {
        this.adoptionRepository = adoptionRepository;
        this.applicationRepository = applicationRepository;
        this.animalRepository = animalRepository;
    }

    public List<AdoptionResponse> GetAllAdoptions() {
        return adoptionRepository.findAll().stream()
                .map(AdoptionResponse::new)
                .collect(Collectors.toList());
    }

    public AdoptionResponse GetAdoptionById(Long id) {
        return adoptionRepository.findById(id)
                .map(AdoptionResponse::new)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
    }

    @Transactional
    public AdoptionResponse addAdoption(AdoptionRequest request) {
        validateApplication(request.getAdoptionApplication());
        validateAdoptionDate(request.getAdoptionDate());
        validateIsActive(request.getIsActive());

        // Hent application fra database
        AdoptionApplication application = applicationRepository
                .findById(request.getAdoptionApplication().getId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Hent animal gennem application
        Animal animal = application.getAnimal();
        if (animal == null) {
            throw new RuntimeException("Animal not found in application");
        }

        if (Status.ADOPTED.equals(animal.getStatus())) {
            throw new RuntimeException("Animal is already adopted");
        }

        if (Status.APPROVED.equals(application.getStatus())) {
            throw new RuntimeException("Application is already approved");
        }

        // Opret adoption
        Adoption adoption = new Adoption();
        adoption.setApplication(application);
        adoption.setAdoptionDate(request.getAdoptionDate());
        adoption.setIsActive(true);

        // Opdater animal status
        animal.setStatus(Status.ADOPTED);

        // Opdater application status
        application.setStatus(Status.APPROVED);

        // Gem alle ændringer (transaction sikrer rollback ved fejl)
        animalRepository.save(animal);
        applicationRepository.save(application);
        adoptionRepository.save(adoption);

        return new AdoptionResponse(adoption);
    }

    private void validateApplication(AdoptionApplication application) {
        if (application == null || application.getId() == null) {
            throw new IllegalArgumentException("Adoption Application cannot be null");
        }
    }

    private void validateAdoptionDate(Date adoptionDate) {
        if (adoptionDate == null) {
            throw new IllegalArgumentException("Adoption date cannot be null");
        }
    }

    private void validateIsActive(Boolean isActive) {
        if (isActive == null) {
            throw new IllegalArgumentException("IsActive cannot be null");
        }
    }

    public AdoptionResponse updateAdoption(Long id, AdoptionRequest request) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));

        adoption.setApplication(request.getAdoptionApplication());
        adoption.setAdoptionDate(request.getAdoptionDate());
        adoption.setIsActive(request.getIsActive());

        adoptionRepository.save(adoption);
        return new AdoptionResponse(adoption);
    }

    public void deleteAdoption(Long id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
        adoptionRepository.delete(adoption);
    }
}