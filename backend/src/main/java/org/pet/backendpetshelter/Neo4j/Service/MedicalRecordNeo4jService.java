package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.MedicalRecordNode;
import org.pet.backendpetshelter.Neo4j.Repository.MedicalRecordNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class MedicalRecordNeo4jService {

    private final MedicalRecordNeo4jRepository medicalRecordRepository;

    public MedicalRecordNeo4jService(MedicalRecordNeo4jRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecordNode> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecordNode getMedicalRecordById(String id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));
    }

    public MedicalRecordNode addMedicalRecord(MedicalRecordNode medicalRecord) {
        if (medicalRecord.getId() == null) {
            medicalRecord.setId(UUID.randomUUID().toString());
        }
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecordNode updateMedicalRecord(String id, MedicalRecordNode request) {
        MedicalRecordNode medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));
        
        medicalRecord.setDate(request.getDate());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setCost(request.getCost());
        medicalRecord.setAnimal(request.getAnimal());
        medicalRecord.setVeterinarian(request.getVeterinarian());
        
        return medicalRecordRepository.save(medicalRecord);
    }

    public void deleteMedicalRecord(String id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException("Medical Record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }
}
