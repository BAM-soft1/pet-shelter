package org.pet.backendpetshelter.Service;

import jakarta.persistence.EntityNotFoundException;
import org.pet.backendpetshelter.DTO.MedicalRecordDTORequest;
import org.pet.backendpetshelter.DTO.MedicalRecordDTOResponse;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Entity.MedicalRecord;
import org.pet.backendpetshelter.Entity.User;
import org.pet.backendpetshelter.Entity.Veterinarian;
import org.pet.backendpetshelter.Reposiotry.AnimalRepository;
import org.pet.backendpetshelter.Repository.MedicalRecordRepository;
import org.pet.backendpetshelter.Reposiotry.VeterinarianRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("mysql")
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AnimalRepository animalRepository;
    private final VeterinarianRepository veterinarianRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, 
                                AnimalRepository animalRepository,
                                VeterinarianRepository veterinarianRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.animalRepository = animalRepository;
        this.veterinarianRepository = veterinarianRepository;
    }

    /* Get all medical records */
    public List<MedicalRecordDTOResponse> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(MedicalRecordDTOResponse::new)
                .toList();
    }

    /* Get specific medical record */
    public MedicalRecordDTOResponse getMedicalRecordById(Long id) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
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
        medicalRecord.setAnimal(reuqest.getAnimal());
        medicalRecord.setVeterinarian(reuqest.getVeterinarian());
        medicalRecord.setDate(reuqest.getDate());
        medicalRecord.setDiagnosis(reuqest.getDiagnosis());
        medicalRecord.setTreatment(reuqest.getTreatment());
        medicalRecord.setCost(reuqest.getCost());
        medicalRecordRepository.save(medicalRecord);
        return new MedicalRecordDTOResponse(medicalRecord);
    }

    /* Update medical record */
    public MedicalRecordDTOResponse updateMedicalRecord(Long id, MedicalRecordDTORequest request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));

        // Hent Animal fra database
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Animal not found with id: " + request.getAnimalId()));

        medicalRecord.setAnimal(animal);
        medicalRecord.setDate(request.getDate());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setCost(request.getCost());

        medicalRecordRepository.save(medicalRecord);
        return new MedicalRecordDTOResponse(medicalRecord);
    }

    /* Delete medical record */
    public void deleteMedicalRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. User not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);

    }
        medicalRecordReposiotry.deleteById(id);
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
}