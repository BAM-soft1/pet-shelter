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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.pet.backendpetshelter.Status;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile({"mysql", "test"})
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

    public Page<AdoptionResponse> GetAllAdoptions(Pageable pageable) {
        return adoptionRepository.findAll(pageable)
                .map(AdoptionResponse::new);
    }

    public AdoptionResponse GetAdoptionById(Long id) {
        return adoptionRepository.findById(id)
                .map(AdoptionResponse::new)
                .orElseThrow(() -> new RuntimeException("Adoption not found with id: " + id));
    }

    @Transactional
    public AdoptionResponse addAdoption(AdoptionRequest request) {

        validateAdoptionDate(request.getAdoptionDate());
        validateIsActive(request.getIsActive());
        validateApplication(request.getAdoptionApplicationId());

        AdoptionApplication application = applicationRepository.findById(request.getAdoptionApplicationId())
                .orElseThrow(() -> new RuntimeException("Adoption Application not found with id: " + request.getAdoptionApplicationId()));



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

        Adoption adoption = new Adoption();
        adoption.setApplication(application);
        adoption.setAdoptionDate(request.getAdoptionDate());
        adoption.setIsActive(true);

        animal.setStatus(Status.ADOPTED);

        application.setStatus(Status.APPROVED);

        animalRepository.save(animal);
        applicationRepository.save(application);
        adoptionRepository.save(adoption);

        return new AdoptionResponse(adoption);
    }

    private void validateApplication(Long applicationId) {
        if (applicationId == null)
            throw new IllegalArgumentException("Adoption Application ID cannot be null");
    }

    private void validateAdoptionDate(Date adoptionDate) {
        if (adoptionDate == null) {
            throw new IllegalArgumentException("Adoption date cannot be null");
        }

        if (adoptionDate.before(new Date())) {
            throw new IllegalArgumentException("Adoption date cannot be in the past.");
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

        AdoptionApplication application = applicationRepository.findById(request.getAdoptionApplicationId())
                .orElseThrow(() -> new RuntimeException("Adoption Application not found with id: " + request.getAdoptionApplicationId()));


        adoption.setApplication(application);
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