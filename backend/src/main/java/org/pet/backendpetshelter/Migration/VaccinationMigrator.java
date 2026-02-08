package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.Vaccination;
import org.pet.backendpetshelter.Mongo.Entity.VaccinationDocument;
import org.pet.backendpetshelter.Mongo.Repository.VaccinationMongoRepository;
import org.pet.backendpetshelter.Repository.VaccinationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("migrate-mongo")
public class VaccinationMigrator implements CommandLineRunner {

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationMongoRepository vaccinationMongoRepository;

    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;

    public VaccinationMigrator(VaccinationRepository vaccinationRepository,
                                VaccinationMongoRepository vaccinationMongoRepository) {
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationMongoRepository = vaccinationMongoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("Vaccination migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting vaccination migration...");

        var vaccinations = vaccinationRepository.findAll();
        var docs = vaccinations.stream().map(this::toDocument).toList();
        vaccinationMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " vaccinations to MongoDB");
    }

    private VaccinationDocument toDocument(Vaccination v) {
        return VaccinationDocument.builder()
                .id(toStringOrNull(v.getId()))
                .animal(v.getAnimal() != null ?
                        VaccinationDocument.EmbeddedAnimal.builder()
                                .id(toStringOrNull(v.getAnimal().getId()))
                                .name(v.getAnimal().getName())
                                .status(v.getAnimal().getStatus() != null ? v.getAnimal().getStatus().name() : null)
                                .build()
                        : null)
                .veterinarian(v.getVeterinarian() != null ?
                        VaccinationDocument.EmbeddedVet.builder()
                                .id(toStringOrNull(v.getVeterinarian().getId()))
                                .firstName(v.getVeterinarian().getUser().getFirstName())
                                .lastName(v.getVeterinarian().getUser().getLastName())
                                .email(v.getVeterinarian().getUser().getEmail())
                                .build()
                        : null)
                .vaccinationType(v.getVaccinationType() != null ?
                        VaccinationDocument.EmbeddedVaccinationType.builder()
                                .id(toStringOrNull(v.getVaccinationType().getId()))
                                .vaccineName(v.getVaccinationType().getVaccineName())
                                .description(v.getVaccinationType().getDescription())
                                .durationMonths(v.getVaccinationType().getDurationMonths())
                                .requiredForAdoption(v.getVaccinationType().getRequiredForAdoption())
                                .build()
                        : null)
                .dateAdministered(v.getDateAdministered())
                .nextDueDate(v.getNextDueDate())
                .build();
    }

    private String toStringOrNull(Long id) {
        return id != null ? id.toString() : null;
    }
}