package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.*;
import org.pet.backendpetshelter.Mongo.Entity.AnimalDocument;
import org.pet.backendpetshelter.Mongo.Repository.AnimalMongoRepository;
import org.pet.backendpetshelter.Repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("migrate-mongo")
@Order(1)
public class AnimalMigrator implements CommandLineRunner {

    private final AnimalRepository animalRepository;
    private final AnimalMongoRepository animalMongoRepository;
    private final VaccinationRepository vaccinationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AdoptionApplicationRepository adoptionApplicationRepository;

    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;

    public AnimalMigrator(AnimalRepository animalRepository,
                          AnimalMongoRepository animalMongoRepository,
                          VaccinationRepository vaccinationRepository,
                          MedicalRecordRepository medicalRecordRepository,
                          AdoptionApplicationRepository adoptionApplicationRepository) {
        this.animalRepository = animalRepository;
        this.animalMongoRepository = animalMongoRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.adoptionApplicationRepository = adoptionApplicationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("Animal migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting animal migration to MongoDB...");

        List<Animal> animals = animalRepository.findAll();
        List<Vaccination> allVaccinations = vaccinationRepository.findAll();
        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.findAll();
        List<AdoptionApplication> allApplications = adoptionApplicationRepository.findAll();

        var docs = animals.stream()
                .map(a -> toDocument(a, allVaccinations, allMedicalRecords, allApplications))
                .toList();

        animalMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " animals to MongoDB");
    }

    private AnimalDocument toDocument(Animal a,
                                       List<Vaccination> allVaccinations,
                                       List<MedicalRecord> allMedicalRecords,
                                       List<AdoptionApplication> allApplications) {

        List<AnimalDocument.EmbeddedVaccination> embeddedVaccinations = allVaccinations.stream()
                .filter(v -> v.getAnimal() != null && v.getAnimal().getId().equals(a.getId()))
                .map(this::toEmbeddedVaccination)
                .collect(Collectors.toList());

        List<AnimalDocument.EmbeddedMedicalRecord> embeddedMedicalRecords = allMedicalRecords.stream()
                .filter(mr -> mr.getAnimal() != null && mr.getAnimal().getId().equals(a.getId()))
                .map(this::toEmbeddedMedicalRecord)
                .collect(Collectors.toList());

        List<AnimalDocument.EmbeddedAdoptionApplication> embeddedApplications = allApplications.stream()
                .filter(app -> app.getAnimal() != null && app.getAnimal().getId().equals(a.getId()))
                .map(this::toEmbeddedApplication)
                .collect(Collectors.toList());

        return AnimalDocument.builder()
                .id(toStringOrNull(a.getId()))
                .name(a.getName())
                .sex(a.getSex())
                .birthDate(a.getBirthDate())
                .intakeDate(a.getIntakeDate())
                .status(a.getStatus() != null ? a.getStatus().name() : null)
                .price(a.getPrice())
                .isActive(a.getIsActive())
                .imageUrl(a.getImageUrl())
                .species(toEmbeddedSpecies(a))
                .breed(toEmbeddedBreed(a))
                .vaccinations(embeddedVaccinations.isEmpty() ? Collections.emptyList() : embeddedVaccinations)
                .medicalRecords(embeddedMedicalRecords.isEmpty() ? Collections.emptyList() : embeddedMedicalRecords)
                .adoptionApplications(embeddedApplications.isEmpty() ? Collections.emptyList() : embeddedApplications)
                .build();
    }

    private AnimalDocument.EmbeddedSpecies toEmbeddedSpecies(Animal a) {
        if (a.getSpecies() == null) return null;
        return AnimalDocument.EmbeddedSpecies.builder()
                .id(toStringOrNull(a.getSpecies().getId()))
                .name(a.getSpecies().getName())
                .build();
    }

    private AnimalDocument.EmbeddedBreed toEmbeddedBreed(Animal a) {
        if (a.getBreed() == null) return null;
        return AnimalDocument.EmbeddedBreed.builder()
                .id(toStringOrNull(a.getBreed().getId()))
                .name(a.getBreed().getName())
                .build();
    }

    private AnimalDocument.EmbeddedVaccination toEmbeddedVaccination(Vaccination v) {
        return AnimalDocument.EmbeddedVaccination.builder()
                .id(toStringOrNull(v.getId()))
                .vaccineName(v.getVaccinationType() != null ? v.getVaccinationType().getVaccineName() : null)
                .description(v.getVaccinationType() != null ? v.getVaccinationType().getDescription() : null)
                .durationMonths(v.getVaccinationType() != null ? v.getVaccinationType().getDurationMonths() : 0)
                .requiredForAdoption(v.getVaccinationType() != null && Boolean.TRUE.equals(v.getVaccinationType().getRequiredForAdoption()))
                .dateAdministered(v.getDateAdministered())
                .nextDueDate(v.getNextDueDate())
                .administeredBy(toEmbeddedVet(v.getVeterinarian()))
                .build();
    }

    private AnimalDocument.EmbeddedMedicalRecord toEmbeddedMedicalRecord(MedicalRecord mr) {
        return AnimalDocument.EmbeddedMedicalRecord.builder()
                .id(toStringOrNull(mr.getId()))
                .date(mr.getDate())
                .diagnosis(mr.getDiagnosis())
                .treatment(mr.getTreatment())
                .cost(mr.getCost())
                .veterinarian(toEmbeddedVet(mr.getVeterinarian()))
                .build();
    }

    private AnimalDocument.EmbeddedAdoptionApplication toEmbeddedApplication(AdoptionApplication app) {
        return AnimalDocument.EmbeddedAdoptionApplication.builder()
                .id(toStringOrNull(app.getId()))
                .applicationDate(app.getApplicationDate())
                .description(app.getDescription())
                .status(app.getStatus() != null ? app.getStatus().name() : null)
                .applicant(toEmbeddedApplicant(app.getUser()))
                .reviewedBy(app.getReviewedByUser() != null ? toEmbeddedApplicant(app.getReviewedByUser()) : null)
                .build();
    }

    private AnimalDocument.EmbeddedApplicant toEmbeddedApplicant(User u) {
        if (u == null) return null;
        return AnimalDocument.EmbeddedApplicant.builder()
                .id(toStringOrNull(u.getId()))
                .name(u.getFirstName() + " " + u.getLastName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .build();
    }

    private AnimalDocument.EmbeddedVet toEmbeddedVet(Veterinarian vet) {
        if (vet == null || vet.getUser() == null) return null;
        return AnimalDocument.EmbeddedVet.builder()
                .id(toStringOrNull(vet.getId()))
                .firstName(vet.getUser().getFirstName())
                .lastName(vet.getUser().getLastName())
                .email(vet.getUser().getEmail())
                .build();
    }

    private String toStringOrNull(Long id) {
        return id != null ? id.toString() : null;
    }
}