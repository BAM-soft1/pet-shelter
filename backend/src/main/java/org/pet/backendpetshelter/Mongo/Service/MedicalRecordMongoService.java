package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.MedicalRecordDocument;
import org.pet.backendpetshelter.Mongo.Repository.MedicalRecordMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class MedicalRecordMongoService {

    private final MedicalRecordMongoRepository medicalRecordRepository;

    public MedicalRecordMongoService(MedicalRecordMongoRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecordDocument> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecordDocument getMedicalRecordById(String id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));
    }

    public MedicalRecordDocument addMedicalRecord(MedicalRecordDocument medicalRecord) {
        if (medicalRecord.getId() == null) {
            medicalRecord.setId(UUID.randomUUID().toString());
        }
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecordDocument updateMedicalRecord(String id, MedicalRecordDocument request) {
        MedicalRecordDocument medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));
        
        medicalRecord.setDate(request.getDate());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setCost(request.getCost());
        medicalRecord.setAnimalId(request.getAnimalId());
        medicalRecord.setVeterinarianId(request.getVeterinarianId());
        
        return medicalRecordRepository.save(medicalRecord);
    }

    public void deleteMedicalRecord(String id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException("Medical Record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }
}
