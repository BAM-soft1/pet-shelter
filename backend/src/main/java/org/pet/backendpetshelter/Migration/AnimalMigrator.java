package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.*;
import org.pet.backendpetshelter.Mongo.Entity.AnimalDocument;
import org.pet.backendpetshelter.Mongo.Repository.AnimalMongoRepository;
import org.pet.backendpetshelter.Repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("migrate-mongo")
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

        System.out.println("Starting animal migration...");

        List<Animal> animals = animalRepository.findAll();
        List<Vaccination> allVaccinations = vaccinationRepository.findAll();
        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.findAll();
        List<AdoptionApplication> allApplications = adoptionApplicationRepository.findAll();

        List<AnimalDocument> docs = animals.stream()
                .map(a -> toDocument(a, allVaccinations, allMedicalRecords, allApplications))
                .collect(Collectors.toList());

        animalMongoRepository.saveAll(docs);
        System.out.println("Migrated " + docs.size() + " animals to MongoDB");
    }

    private AnimalDocument toDocument(Animal a,
                                       List<Vaccination> allVaccinations,
                                       List<MedicalRecord> allMedicalRecords,
                                       List<AdoptionApplication> allApplications) {

        List<AnimalDocument.EmbeddedVaccination> vaccinations = allVaccinations.stream()
                .filter(v -> v.getAnimal() != null && v.getAnimal().getId().equals(a.getId()))
                .map(this::toEmbeddedVaccination)
                .collect(Collectors.toList());

        List<AnimalDocument.EmbeddedMedicalRecord> medicalRecords = allMedicalRecords.stream()
                .filter(r -> r.getAnimal() != null && r.getAnimal().getId().equals(a.getId()))
                .map(this::toEmbeddedMedicalRecord)
                .collect(Collectors.toList());

        List<AnimalDocument.EmbeddedAdoptionApplication> applications = allApplications.stream()
                .filter(app -> app.getAnimal() != null && app.getAnimal().getId().equals(a.getId()))
                .map(this::toEmbeddedApplication)
                .collect(Collectors.toList());

        return AnimalDocument.builder()
                .id(a.getId().toString())
                .name(a.getName())
                .sex(a.getSex())
                .birthDate(a.getBirthDate())
                .intakeDate(a.getIntakeDate())
                .status(a.getStatus() != null ? a.getStatus().name() : null)
                .price(a.getPrice())
                .isActive(a.getIsActive() != null ? a.getIsActive() : true)
                .imageUrl(a.getImageUrl())
                .species(a.getSpecies() != null ?
                        AnimalDocument.EmbeddedSpecies.builder()
                                .name(a.getSpecies().getName())
                                .description(null)
                                .build()
                        : null)
                .breed(a.getBreed() != null ?
                        AnimalDocument.EmbeddedBreed.builder()
                                .name(a.getBreed().getName())
                                .speciesName(a.getSpecies() != null ? a.getSpecies().getName() : null)
                                .build()
                        : null)
                .vaccinations(vaccinations)
                .medicalRecords(medicalRecords)
                .adoptionApplications(applications)
                .build();
    }

    private AnimalDocument.EmbeddedVaccination toEmbeddedVaccination(Vaccination v) {
        AnimalDocument.EmbeddedVet vet = null;
        if (v.getVeterinarian() != null && v.getVeterinarian().getUser() != null) {
            User u = v.getVeterinarian().getUser();
            vet = AnimalDocument.EmbeddedVet.builder()
                    .firstName(u.getFirstName())
                    .lastName(u.getLastName())
                    .email(u.getEmail())
                    .build();
        }

        return AnimalDocument.EmbeddedVaccination.builder()
                .vaccineName(v.getVaccinationType() != null ? v.getVaccinationType().getVaccineName() : null)
                .description(v.getVaccinationType() != null ? v.getVaccinationType().getDescription() : null)
                .durationMonths(v.getVaccinationType() != null ? v.getVaccinationType().getDurationMonths() : 0)
                .requiredForAdoption(v.getVaccinationType() != null && Boolean.TRUE.equals(v.getVaccinationType().getRequiredForAdoption()))
                .dateAdministered(v.getDateAdministered())
                .nextDueDate(v.getNextDueDate())
                .administeredBy(vet)
                .build();
    }

    private AnimalDocument.EmbeddedMedicalRecord toEmbeddedMedicalRecord(MedicalRecord r) {
        AnimalDocument.EmbeddedVet vet = null;
        if (r.getVeterinarian() != null && r.getVeterinarian().getUser() != null) {
            User u = r.getVeterinarian().getUser();
            vet = AnimalDocument.EmbeddedVet.builder()
                    .firstName(u.getFirstName())
                    .lastName(u.getLastName())
                    .email(u.getEmail())
                    .build();
        }

        return AnimalDocument.EmbeddedMedicalRecord.builder()
                .date(r.getDate())
                .diagnosis(r.getDiagnosis())
                .treatment(r.getTreatment())
                .cost(r.getCost())
                .veterinarian(vet)
                .build();
    }

    private AnimalDocument.EmbeddedAdoptionApplication toEmbeddedApplication(AdoptionApplication app) {
        AnimalDocument.EmbeddedApplicant applicant = null;
        if (app.getUser() != null) {
            applicant = AnimalDocument.EmbeddedApplicant.builder()
                    .name(app.getUser().getFirstName() + " " + app.getUser().getLastName())
                    .email(app.getUser().getEmail())
                    .phone(app.getUser().getPhone())
                    .build();
        }

        return AnimalDocument.EmbeddedAdoptionApplication.builder()
                .applicationDate(app.getApplicationDate())
                .status(app.getStatus() != null ? app.getStatus().name() : null)
                .description(app.getDescription())
                .applicant(applicant)
                .build();
    }
}