package org.pet.backendpetshelter.Service;

import jakarta.persistence.EntityNotFoundException;
import org.pet.backendpetshelter.DTO.MedicalRecordDTORequest;
import org.pet.backendpetshelter.DTO.MedicalRecordDTOResponse;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Entity.MedicalRecord;
import org.pet.backendpetshelter.Entity.User;
import org.pet.backendpetshelter.Entity.Veterinarian;
import org.pet.backendpetshelter.Reposiotry.AnimalRepository;
import org.pet.backendpetshelter.Reposiotry.MedicalRecordReposiotry;
import org.pet.backendpetshelter.Reposiotry.VeterinarianRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordReposiotry medicalRecordReposiotry;
    private final AnimalRepository animalRepository;
    private final VeterinarianRepository veterinarianRepository;

    public MedicalRecordService(MedicalRecordReposiotry medicalRecordReposiotry, 
                                AnimalRepository animalRepository,
                                VeterinarianRepository veterinarianRepository) {
        this.medicalRecordReposiotry = medicalRecordReposiotry;
        this.animalRepository = animalRepository;
        this.veterinarianRepository = veterinarianRepository;
    }

    /* Get all medical records */
    public List<MedicalRecordDTOResponse> getAllMedicalRecords() {
        return medicalRecordReposiotry.findAll().stream()
                .map(MedicalRecordDTOResponse::new)
                .toList();
    }

    /* Get specific medical record */
    public MedicalRecordDTOResponse getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = medicalRecordReposiotry.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));
        return new MedicalRecordDTOResponse(medicalRecord);
    }

    /* Get medical records by animal id */
    public List<MedicalRecordDTOResponse> getMedicalRecordsByAnimalId(Long animalId) {
        return medicalRecordReposiotry.findByAnimalId(animalId).stream()
                .map(MedicalRecordDTOResponse::new)
                .toList();
    }

    /* Add medical record */
    public MedicalRecordDTOResponse addMedicalRecord(MedicalRecordDTORequest request) {
        // Hent Animal fra database
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Animal not found with id: " + request.getAnimalId()));

        // Hent Veterinarian fra den indloggede bruger
        Veterinarian veterinarian = getAuthenticatedVeterinarian();

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setAnimal(animal);
        medicalRecord.setVeterinarian(veterinarian);
        medicalRecord.setDate(request.getDate());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setCost(request.getCost());
        
        medicalRecordReposiotry.save(medicalRecord);
        return new MedicalRecordDTOResponse(medicalRecord);
    }

    /* Update medical record */
    public MedicalRecordDTOResponse updateMedicalRecord(Long id, MedicalRecordDTORequest request) {
        MedicalRecord medicalRecord = medicalRecordReposiotry.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));

        // Hent Animal fra database
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Animal not found with id: " + request.getAnimalId()));

        medicalRecord.setAnimal(animal);
        medicalRecord.setDate(request.getDate());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setCost(request.getCost());

        medicalRecordReposiotry.save(medicalRecord);
        return new MedicalRecordDTOResponse(medicalRecord);
    }

    /* Delete medical record */
    public void deleteMedicalRecord(Long id) {
        if (!medicalRecordReposiotry.existsById(id)) {  // Rettet logik
            throw new EntityNotFoundException("Cannot delete. Medical Record not found with id: " + id);
        }
        medicalRecordReposiotry.deleteById(id);
    }

    /* Helper method to get authenticated veterinarian */
    private Veterinarian getAuthenticatedVeterinarian() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Find veterinarian baseret på email
        return veterinarianRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Veterinarian not found for user: " + email));
    }
}