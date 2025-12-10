package org.pet.backendpetshelter.Service;

import jakarta.persistence.EntityNotFoundException;
import org.pet.backendpetshelter.DTO.VaccinationRequest;
import org.pet.backendpetshelter.DTO.VaccinationResponse;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Entity.Vaccination;
import org.pet.backendpetshelter.Entity.VaccinationType;
import org.pet.backendpetshelter.Entity.Veterinarian;
import org.pet.backendpetshelter.Repository.AnimalRepository;
import org.pet.backendpetshelter.Repository.VaccinationRepository;
import org.pet.backendpetshelter.Repository.VaccinationTypeRepository;
import org.pet.backendpetshelter.Repository.VeterinarianRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Profile("mysql")
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final AnimalRepository animalRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final VaccinationTypeRepository vaccinationTypeRepository;

    public VaccinationService(
            VaccinationRepository vaccinationRepository,
            AnimalRepository animalRepository,
            VeterinarianRepository veterinarianRepository,
            VaccinationTypeRepository vaccinationTypeRepository) {
        this.vaccinationRepository = vaccinationRepository;
        this.animalRepository = animalRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
    }

    /* Get all vaccinations */
    public List<VaccinationResponse> GetAllVaccinations() {
        return vaccinationRepository.findAll().stream()
                .map(VaccinationResponse::new)
                .toList();
    }

    /* Get specific vaccination */
    public VaccinationResponse GetVaccinationById(Long id) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
        return new VaccinationResponse(vaccination);
    }

    /* Get vaccinations by animal id */
    public List<VaccinationResponse> getVaccinationsByAnimalId(Long animalId) {
        return vaccinationRepository.findByAnimalId(animalId).stream()
                .map(VaccinationResponse::new)
                .toList();
    }

    /* Add vaccination */
    public VaccinationResponse addVaccination(VaccinationRequest request) {
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Animal not found with id: " + request.getAnimalId()));

        VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination type not found with id: " + request.getVaccinationTypeId()));

        Veterinarian veterinarian = getAuthenticatedVeterinarian();

        Vaccination vaccination = new Vaccination();
        vaccination.setAnimal(animal);
        vaccination.setVeterinarian(veterinarian);
        vaccination.setVaccinationType(vaccinationType);
        vaccination.setDateAdministered(parseDate(request.getDateAdministered()));
        vaccination.setNextDueDate(parseDate(request.getNextDueDate()));

        vaccinationRepository.save(vaccination);
        return new VaccinationResponse(vaccination);
    }

    /* Update vaccination */
    public VaccinationResponse updateVaccination(Long id, VaccinationRequest request) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));

        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Animal not found with id: " + request.getAnimalId()));

        VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination type not found with id: " + request.getVaccinationTypeId()));

        vaccination.setAnimal(animal);
        vaccination.setVaccinationType(vaccinationType);
        vaccination.setDateAdministered(parseDate(request.getDateAdministered()));
        vaccination.setNextDueDate(parseDate(request.getNextDueDate()));

        vaccinationRepository.save(vaccination);
        return new VaccinationResponse(vaccination);
    }

    /* Delete vaccination */
    public void deleteVaccination(Long id) {
        if (!vaccinationRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Vaccination not found with id: " + id);
        }
        vaccinationRepository.deleteById(id);
    }

    private Veterinarian getAuthenticatedVeterinarian() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Veterinarian vet = veterinarianRepository.findByUser_Email(email);
        if (vet == null) {
            throw new RuntimeException("Veterinarian not found for user: " + email);
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