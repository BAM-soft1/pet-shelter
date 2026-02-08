package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.MedicalRecord;
import org.pet.backendpetshelter.Mongo.Entity.MedicalRecordDocument;
import org.pet.backendpetshelter.Mongo.Repository.MedicalRecordMongoRepository;
import org.pet.backendpetshelter.Repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("migrate-mongo")
public class MedicalRecordMigrator implements CommandLineRunner {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMongoRepository medicalRecordMongoRepository;

    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;

    public MedicalRecordMigrator(MedicalRecordRepository medicalRecordRepository,
                                  MedicalRecordMongoRepository medicalRecordMongoRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordMongoRepository = medicalRecordMongoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("MedicalRecord migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting medical record migration...");

        var records = medicalRecordRepository.findAll();
        var docs = records.stream().map(this::toDocument).toList();
        medicalRecordMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " medical records to MongoDB");
    }

    private MedicalRecordDocument toDocument(MedicalRecord r) {
        return MedicalRecordDocument.builder()
                .id(toStringOrNull(r.getId()))
                .animal(r.getAnimal() != null ?
                        MedicalRecordDocument.EmbeddedAnimal.builder()
                                .id(toStringOrNull(r.getAnimal().getId()))
                                .name(r.getAnimal().getName())
                                .build()
                        : null)
                .veterinarian(r.getVeterinarian() != null ?
                        MedicalRecordDocument.EmbeddedVet.builder()
                                .id(toStringOrNull(r.getVeterinarian().getId()))
                                .firstName(r.getVeterinarian().getUser().getFirstName())
                                .lastName(r.getVeterinarian().getUser().getLastName())
                                .email(r.getVeterinarian().getUser().getEmail())
                                .build()
                        : null)
                .date(r.getDate())
                .diagnosis(r.getDiagnosis())
                .treatment(r.getTreatment())
                .cost(r.getCost())
                .build();
    }

    private String toStringOrNull(Long id) {
        return id != null ? id.toString() : null;
    }
}