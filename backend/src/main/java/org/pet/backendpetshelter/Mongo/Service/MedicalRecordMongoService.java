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

    public List<MedicalRecordDocument> getAll() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecordDocument getById(String id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + id));
    }

    public MedicalRecordDocument create(MedicalRecordDocument record) {
        if (record.getId() == null) {
            record.setId(UUID.randomUUID().toString());
        }
        return medicalRecordRepository.save(record);
    }

    public MedicalRecordDocument update(String id, MedicalRecordDocument request) {
        MedicalRecordDocument existing = getById(id);
        existing.setAnimal(request.getAnimal());
        existing.setVeterinarian(request.getVeterinarian());
        existing.setDate(request.getDate());
        existing.setDiagnosis(request.getDiagnosis());
        existing.setTreatment(request.getTreatment());
        existing.setCost(request.getCost());
        return medicalRecordRepository.save(existing);
    }

    public void delete(String id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException("Medical record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }
}