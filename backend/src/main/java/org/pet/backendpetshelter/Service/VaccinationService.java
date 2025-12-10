package org.pet.backendpetshelter.Service;

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
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<VaccinationResponse> GetAllVaccinations() {
        return vaccinationRepository.findAllWithRelations().stream()
                .map(VaccinationResponse::new)
                .collect(Collectors.toList());
    }

    public VaccinationResponse GetVaccinationById(Long id) {
        Vaccination vaccination = vaccinationRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
        return new VaccinationResponse(vaccination);
    }

    public VaccinationResponse addVaccination(VaccinationRequest request) {
        Vaccination vaccination = new Vaccination();

        // Hent Animal fra database
        if (request.getAnimalId() != null) {
            Animal animal = animalRepository.findById(request.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("Animal not found with id: " + request.getAnimalId()));
            vaccination.setAnimal(animal);
        }

        // Hent Veterinarian fra database baseret på user ID
        if (request.getUserId() != null) {
            Veterinarian veterinarian = veterinarianRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Veterinarian not found for user id: " + request.getUserId()));
            vaccination.setVeterinarian(veterinarian);
        }

        // Hent VaccinationType fra database
        if (request.getVaccinationTypeId() != null) {
            VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                    .orElseThrow(() -> new RuntimeException("Vaccination type not found with id: " + request.getVaccinationTypeId()));
            vaccination.setVaccinationType(vaccinationType);
        }

        // Parse datoer
        vaccination.setDateAdministered(parseDate(request.getDateAdministered()));
        vaccination.setNextDueDate(parseDate(request.getNextDueDate()));

        vaccinationRepository.save(vaccination);
        return new VaccinationResponse(vaccination);
    }

    public VaccinationResponse updateVaccination(Long id, VaccinationRequest request) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));

        // Hent Animal fra database
        if (request.getAnimalId() != null) {
            Animal animal = animalRepository.findById(request.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("Animal not found with id: " + request.getAnimalId()));
            vaccination.setAnimal(animal);
        }

        // Hent Veterinarian fra database
        if (request.getUserId() != null) {
            Veterinarian veterinarian = veterinarianRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Veterinarian not found for user id: " + request.getUserId()));
            vaccination.setVeterinarian(veterinarian);
        }

        // Hent VaccinationType fra database
        if (request.getVaccinationTypeId() != null) {
            VaccinationType vaccinationType = vaccinationTypeRepository.findById(request.getVaccinationTypeId())
                    .orElseThrow(() -> new RuntimeException("Vaccination type not found with id: " + request.getVaccinationTypeId()));
            vaccination.setVaccinationType(vaccinationType);
        }

        // Parse datoer
        vaccination.setDateAdministered(parseDate(request.getDateAdministered()));
        vaccination.setNextDueDate(parseDate(request.getNextDueDate()));

        vaccinationRepository.save(vaccination);
        return new VaccinationResponse(vaccination);
    }

    public void deleteVaccination(Long id) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
        vaccinationRepository.delete(vaccination);
    }

    private java.util.Date parseDate(String dateString) {
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