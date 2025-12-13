package org.pet.backendpetshelter.Service;

import org.pet.backendpetshelter.DTO.VaccinationRequest;
import org.pet.backendpetshelter.DTO.VaccinationResponse;
import org.pet.backendpetshelter.Entity.Vaccination;
import org.pet.backendpetshelter.Repository.VaccinationRepository;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Entity.VaccinationType;
import org.pet.backendpetshelter.Entity.Veterinarian;
import org.pet.backendpetshelter.Repository.AnimalRepository;
import org.pet.backendpetshelter.Repository.VaccinationTypeRepository;
import org.pet.backendpetshelter.Repository.VeterinarianRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Profile({"mysql", "test"})
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final AnimalRepository animalRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final VaccinationTypeRepository vaccinationTypeRepository;

    public VaccinationService(VaccinationRepository vaccinationRepository,
                              AnimalRepository animalRepository,
                              VeterinarianRepository veterinarianRepository,
                              VaccinationTypeRepository vaccinationTypeRepository) {
        this.vaccinationRepository = vaccinationRepository;
        this.animalRepository = animalRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
    }

    /* Get All Vaccinations */
    public Page<VaccinationResponse> GetAllVaccinations(Pageable pageable) {
        return vaccinationRepository.findAll(pageable)
                .map(VaccinationResponse::new);
    }
    
    /* Get All Vaccinations with Filters */
    public Page<VaccinationResponse> GetAllVaccinationsWithFilters(
            String animalStatus,
            String search,
            Pageable pageable) {
        
        return vaccinationRepository.findAllWithFilters(animalStatus, search, pageable)
                .map(VaccinationResponse::new);
    }

    public VaccinationResponse GetVaccinationById(Long id) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination not found with id: " + id));
        return new VaccinationResponse(vaccination);
    }

    public VaccinationResponse addVaccination(VaccinationRequest request) {
        // Validate required fields
        if (request.getAnimalId() == null) {
            throw new IllegalArgumentException("Animal ID is required");
        }
        if (request.getVaccinationTypeId() == null) {
            throw new IllegalArgumentException("Vaccination Type ID is required");
        }
        
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Animal not found with id: " + request.getAnimalId()));

        VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination Type not found with id: " + request.getVaccinationTypeId()));

        Veterinarian veterinarian = getAuthenticatedVeterinarian();

        Vaccination vaccination = new Vaccination();
        vaccination.setAnimal(animal);
        vaccination.setVeterinarian(veterinarian);
        vaccination.setDateAdministered(parseDate(request.getDateAdministered()));
        vaccination.setVaccinationType(vaccinationType);
        vaccination.setNextDueDate(parseDate(request.getNextDueDate()));

        vaccinationRepository.save(vaccination);
        return new VaccinationResponse(vaccination);
    }

    public VaccinationResponse updateVaccination(Long id, VaccinationRequest request) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination not found with id: " + id));

        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Animal not found with id: " + request.getAnimalId()));

        VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination Type not found with id: " + request.getVaccinationTypeId()));

        vaccination.setAnimal(animal);
        vaccination.setDateAdministered(parseDate(request.getDateAdministered()));
        vaccination.setVaccinationType(vaccinationType);
        vaccination.setNextDueDate(parseDate(request.getNextDueDate()));

        vaccinationRepository.save(vaccination);
        return new VaccinationResponse(vaccination);
    }

    public void deleteVaccination(Long id) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination not found with id: " + id));
        vaccinationRepository.delete(vaccination);
    }

    private Veterinarian getAuthenticatedVeterinarian() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Veterinarian vet = veterinarianRepository.findByUser_Email(email);
        if (vet == null) {
            throw new EntityNotFoundException("Veterinarian not found for user: " + email);
        }
        return vet;
    }

    private Date parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + dateString);
        }
    }
}