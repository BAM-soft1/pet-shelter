package org.pet.backendpetshelter.Service;


import org.pet.backendpetshelter.DTO.AdminAdoptionApplicationResponse;
import org.pet.backendpetshelter.DTO.AdoptionApplicationRequest;
import org.pet.backendpetshelter.DTO.AdoptionApplicationResponse;
import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Entity.User;
import org.pet.backendpetshelter.Repository.AdoptionApplicationRepository;
import org.pet.backendpetshelter.Repository.AnimalRepository;
import org.pet.backendpetshelter.Repository.UserRepository;
import org.pet.backendpetshelter.Status;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Profile("mysql")
public class AdoptionApplicationService
{
    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;

    public AdoptionApplicationService(AdoptionApplicationRepository adoptionApplicationRepository, UserRepository userRepository, AnimalRepository animalRepository) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.adoptionApplicationRepository = adoptionApplicationRepository;
    }

    public List<AdminAdoptionApplicationResponse> GetAllAdoptionApplications() {
        return adoptionApplicationRepository.findAll().stream()
                .map(AdminAdoptionApplicationResponse::new)
                .toList();
    }

    public AdoptionApplicationResponse GetAdoptionApplicationById(Long id) {
        AdoptionApplication application = adoptionApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find application with id: " + id));
        return new AdoptionApplicationResponse(application);
    }

    public List<AdoptionApplicationResponse> getAdoptionApplicationsForUser(Long userId) {
        // Optional: verify user exists first
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return adoptionApplicationRepository.findByUserId(userId).stream()
                .map(AdoptionApplicationResponse::new)
                .toList();
    }

    public AdoptionApplicationResponse addAdoptionApplication(AdoptionApplicationRequest request) {
        // 1. Find user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // 2. Find animal
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal not found with id: " + request.getAnimalId()));

        // 3. Check if user has already applied for this animal (proactive check)
        if (adoptionApplicationRepository.existsByUserIdAndAnimalId(user.getId(), animal.getId())) {
            throw new IllegalStateException("You have already submitted an application for this animal");
        }

        // 4. Create application and set fields
        AdoptionApplication application = new AdoptionApplication();
        application.setUser(user);
        application.setAnimal(animal);
        application.setDescription(request.getDescription());
        application.setApplicationDate(new Date());        // now
        application.setStatus(Status.PENDING);            // default
        application.setReviewedByUser(null);              // default
        application.setIsActive(true);                    // default

        // 5. Save and return response DTO
        try {
            adoptionApplicationRepository.save(application);
            return new AdoptionApplicationResponse(application);
        } catch (DataIntegrityViolationException e) {
            // Catch duplicate constraint violations (race condition safety net)
            if (e.getMessage().contains("UKd3eita4oito3bvhios11sl3lf") ||
                    e.getMessage().contains("Duplicate entry")) {
                throw new IllegalStateException("You have already submitted an application for this animal");
            }
            // Re-throw if it's a different constraint violation
            throw e;
        }
    }

    /* Update Adoption Application */
    public AdoptionApplicationResponse updateAdoptionApplication(Long id, AdoptionApplicationRequest request) {
        AdoptionApplication application = adoptionApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find application with id: " + id));

        // If you want to allow changing user/animal (usually not needed)
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
            application.setUser(user);
        }

        if (request.getAnimalId() != null) {
            Animal animal = animalRepository.findById(request.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("Animal not found with id: " + request.getAnimalId()));
            application.setAnimal(animal);
        }

        if (request.getDescription() != null) {
            application.setDescription(request.getDescription());
        }


        adoptionApplicationRepository.save(application);
        return new AdoptionApplicationResponse(application);
    }

    /* Delete Adoption Application */
    public void deleteAdoptionApplication(Long id) {
        AdoptionApplication application = adoptionApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find application with id: " + id));
        adoptionApplicationRepository.delete(application);
    }

    public Boolean hasUserAppliedForAnimal(Long userId, Long animalId) {
        return adoptionApplicationRepository.existsByUserIdAndAnimalId(userId, animalId);
    }

    public AdoptionApplicationResponse rejectAdoptionApplication(Long id, Long reviewedByUserId) {
        AdoptionApplication application = adoptionApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find application with id: " + id));

        User reviewer = userRepository.findById(reviewedByUserId)
                .orElseThrow(() -> new RuntimeException("Reviewer user not found with id: " + reviewedByUserId));

        application.setStatus(Status.REJECTED);
        application.setReviewedByUser(reviewer);

        adoptionApplicationRepository.save(application);
        return new AdoptionApplicationResponse(application);
    }
}