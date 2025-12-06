package org.pet.backendpetshelter.Neo4jMigration;

import org.pet.backendpetshelter.Configuration.RefreshToken;
import org.pet.backendpetshelter.Entity.*;
import org.pet.backendpetshelter.Neo4j.Entity.*;
import org.pet.backendpetshelter.Neo4j.Repository.*;
import org.pet.backendpetshelter.Repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(100) // Run after other initializations
@Profile("migrate-neo4j")
public class Neo4jMigrator implements CommandLineRunner {

    @Value("${migration.neo4j.enabled:false}")
    private boolean migrationEnabled;

    // JPA Repositories
    private final UserRepository userRepository;
    private final SpeciesRepository speciesRepository;
    private final BreedRepository breedRepository;
    private final AnimalRepository animalRepository;
    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final AdoptionRepository adoptionRepository;
    private final FosterCareRepository fosterCareRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final VaccinationTypeRepository vaccinationTypeRepository;
    private final VaccinationRepository vaccinationRepository;
    private final VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // Neo4j Repositories
    private final UserNeo4jRepository userNeo4jRepository;
    private final SpeciesNeo4jRepository speciesNeo4jRepository;
    private final BreedNeo4jRepository breedNeo4jRepository;
    private final AnimalNeo4jRepository animalNeo4jRepository;
    private final AdoptionApplicationNeo4jRepository adoptionApplicationNeo4jRepository;
    private final AdoptionNeo4jRepository adoptionNeo4jRepository;
    private final FosterCareNeo4jRepository fosterCareNeo4jRepository;
    private final VeterinarianNeo4jRepository veterinarianNeo4jRepository;
    private final MedicalRecordNeo4jRepository medicalRecordNeo4jRepository;
    private final VaccinationTypeNeo4jRepository vaccinationTypeNeo4jRepository;
    private final VaccinationNeo4jRepository vaccinationNeo4jRepository;
    private final VaccineTypeSpeciesNeo4jRepository vaccineTypeSpeciesNeo4jRepository;
    private final RefreshTokenNeo4jRepository refreshTokenNeo4jRepository;

    // Caches for relationship lookups
    private final Map<Long, UserNode> userCache = new HashMap<>();
    private final Map<Long, SpeciesNode> speciesCache = new HashMap<>();
    private final Map<Long, BreedNode> breedCache = new HashMap<>();
    private final Map<Long, AnimalNode> animalCache = new HashMap<>();
    private final Map<Long, AdoptionApplicationNode> applicationCache = new HashMap<>();
    private final Map<Long, VeterinarianNode> vetCache = new HashMap<>();
    private final Map<Long, VaccinationTypeNode> vaccinationTypeCache = new HashMap<>();

    public Neo4jMigrator(
            UserRepository userRepository,
            SpeciesRepository speciesRepository,
            BreedRepository breedRepository,
            AnimalRepository animalRepository,
            AdoptionApplicationRepository adoptionApplicationRepository,
            AdoptionRepository adoptionRepository,
            FosterCareRepository fosterCareRepository,
            VeterinarianRepository veterinarianRepository,
            MedicalRecordRepository medicalRecordRepository,
            VaccinationTypeRepository vaccinationTypeRepository,
            VaccinationRepository vaccinationRepository,
            VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository,
            RefreshTokenRepository refreshTokenRepository,
            UserNeo4jRepository userNeo4jRepository,
            SpeciesNeo4jRepository speciesNeo4jRepository,
            BreedNeo4jRepository breedNeo4jRepository,
            AnimalNeo4jRepository animalNeo4jRepository,
            AdoptionApplicationNeo4jRepository adoptionApplicationNeo4jRepository,
            AdoptionNeo4jRepository adoptionNeo4jRepository,
            FosterCareNeo4jRepository fosterCareNeo4jRepository,
            VeterinarianNeo4jRepository veterinarianNeo4jRepository,
            MedicalRecordNeo4jRepository medicalRecordNeo4jRepository,
            VaccinationTypeNeo4jRepository vaccinationTypeNeo4jRepository,
            VaccinationNeo4jRepository vaccinationNeo4jRepository,
            VaccineTypeSpeciesNeo4jRepository vaccineTypeSpeciesNeo4jRepository,
            RefreshTokenNeo4jRepository refreshTokenNeo4jRepository) {
        this.userRepository = userRepository;
        this.speciesRepository = speciesRepository;
        this.breedRepository = breedRepository;
        this.animalRepository = animalRepository;
        this.adoptionApplicationRepository = adoptionApplicationRepository;
        this.adoptionRepository = adoptionRepository;
        this.fosterCareRepository = fosterCareRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.vaccineTypeSpeciesRepository = vaccineTypeSpeciesRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userNeo4jRepository = userNeo4jRepository;
        this.speciesNeo4jRepository = speciesNeo4jRepository;
        this.breedNeo4jRepository = breedNeo4jRepository;
        this.animalNeo4jRepository = animalNeo4jRepository;
        this.adoptionApplicationNeo4jRepository = adoptionApplicationNeo4jRepository;
        this.adoptionNeo4jRepository = adoptionNeo4jRepository;
        this.fosterCareNeo4jRepository = fosterCareNeo4jRepository;
        this.veterinarianNeo4jRepository = veterinarianNeo4jRepository;
        this.medicalRecordNeo4jRepository = medicalRecordNeo4jRepository;
        this.vaccinationTypeNeo4jRepository = vaccinationTypeNeo4jRepository;
        this.vaccinationNeo4jRepository = vaccinationNeo4jRepository;
        this.vaccineTypeSpeciesNeo4jRepository = vaccineTypeSpeciesNeo4jRepository;
        this.refreshTokenNeo4jRepository = refreshTokenNeo4jRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("Neo4j migration disabled. Set migration.neo4j.enabled=true to run.");
            return;
        }

        System.out.println("Starting Neo4j migration from MySQL...");

        // Migrate in order of dependencies (entities without relationships first)
        migrateUsers();
        migrateSpecies();
        migrateVaccinationTypes();
        migrateBreeds();           // depends on Species
        migrateVeterinarians();    // depends on User
        migrateAnimals();          // depends on Species, Breed
        migrateAdoptionApplications(); // depends on User, Animal
        migrateAdoptions();        // depends on User, Animal, AdoptionApplication
        migrateFosterCare();       // depends on User, Animal
        migrateMedicalRecords();   // depends on Animal, Veterinarian
        migrateVaccinations();     // depends on Animal, Veterinarian, VaccinationType
        migrateVaccineTypeSpecies(); // depends on Species, VaccinationType
        migrateRefreshTokens();    // depends on User

        System.out.println("Neo4j migration completed!");
    }

    private void migrateUsers() {
        var users = userRepository.findAll();
        var nodes = users.stream().map(u -> {
            var node = UserNode.builder()
                    .id(toStringOrNull(u.getId()))
                    .email(u.getEmail())
                    .password(u.getPassword())
                    .firstName(u.getFirstName())
                    .lastName(u.getLastName())
                    .phone(u.getPhone())
                    .isActive(u.getIsActive())
                    .role(u.getRole())
                    .build();
            userCache.put(u.getId(), node);
            return node;
        }).toList();
        userNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " users to Neo4j");
    }

    private void migrateSpecies() {
        var species = speciesRepository.findAll();
        var nodes = species.stream().map(s -> {
            var node = SpeciesNode.builder()
                    .id(toStringOrNull(s.getId()))
                    .name(s.getName())
                    .build();
            speciesCache.put(s.getId(), node);
            return node;
        }).toList();
        speciesNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " species to Neo4j");
    }

    private void migrateVaccinationTypes() {
        var types = vaccinationTypeRepository.findAll();
        var nodes = types.stream().map(t -> {
            var node = VaccinationTypeNode.builder()
                    .id(toStringOrNull(t.getId()))
                    .vaccineName(t.getVaccineName())
                    .description(t.getDescription())
                    .durationMonths(t.getDurationMonths())
                    .requiredForAdoption(t.getRequiredForAdoption())
                    .build();
            vaccinationTypeCache.put(t.getId(), node);
            return node;
        }).toList();
        vaccinationTypeNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " vaccination types to Neo4j");
    }

    private void migrateBreeds() {
        var breeds = breedRepository.findAll();
        var nodes = breeds.stream().map(b -> {
            var node = BreedNode.builder()
                    .id(toStringOrNull(b.getId()))
                    .name(b.getName())
                    .species(b.getSpecies() != null ? speciesCache.get(b.getSpecies().getId()) : null)
                    .build();
            breedCache.put(b.getId(), node);
            return node;
        }).toList();
        breedNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " breeds to Neo4j");
    }

    private void migrateVeterinarians() {
        var vets = veterinarianRepository.findAll();
        var nodes = vets.stream().map(v -> {
            var node = VeterinarianNode.builder()
                    .id(toStringOrNull(v.getId()))
                    .licenseNumber(v.getLicenseNumber())
                    .clinicName(v.getClinicName())
                    .isActive(v.getIsActive())
                    .user(v.getUser() != null ? userCache.get(v.getUser().getId()) : null)
                    .build();
            vetCache.put(v.getId(), node);
            return node;
        }).toList();
        veterinarianNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " veterinarians to Neo4j");
    }

    private void migrateAnimals() {
        var animals = animalRepository.findAll();
        var nodes = animals.stream().map(a -> {
            var node = AnimalNode.builder()
                    .id(toStringOrNull(a.getId()))
                    .name(a.getName())
                    .birthDate(a.getBirthDate())
                    .sex(a.getSex())
                    .intakeDate(a.getIntakeDate())
                    .status(a.getStatus() != null ? a.getStatus().name() : null)
                    .price(a.getPrice())
                    .isActive(a.getIsActive())
                    .imageUrl(a.getImageUrl())
                    .species(a.getSpecies() != null ? speciesCache.get(a.getSpecies().getId()) : null)
                    .breed(a.getBreed() != null ? breedCache.get(a.getBreed().getId()) : null)
                    .build();
            animalCache.put(a.getId(), node);
            return node;
        }).toList();
        animalNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " animals to Neo4j");
    }

    private void migrateAdoptionApplications() {
        var applications = adoptionApplicationRepository.findAll();
        var nodes = applications.stream().map(a -> {
            var node = AdoptionApplicationNode.builder()
                    .id(toStringOrNull(a.getId()))
                    .applicationDate(a.getApplicationDate())
                    .status(a.getStatus())
                    .isActive(a.getIsActive())
                    .user(a.getUser() != null ? userCache.get(a.getUser().getId()) : null)
                    .animal(a.getAnimal() != null ? animalCache.get(a.getAnimal().getId()) : null)
                    .reviewedByUser(a.getReviewedByUser() != null ? userCache.get(a.getReviewedByUser().getId()) : null)
                    .build();
            applicationCache.put(a.getId(), node);
            return node;
        }).toList();
        adoptionApplicationNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " adoption applications to Neo4j");
    }

    private void migrateAdoptions() {
        var adoptions = adoptionRepository.findAll();
        var nodes = adoptions.stream().map(a -> AdoptionNode.builder()
                .id(toStringOrNull(a.getId()))
                .adoptionDate(a.getAdoptionDate())
                .isActive(a.getIsActive())
                .adoptionUser(a.getAdoptionUser() != null ? userCache.get(a.getAdoptionUser().getId()) : null)
                .animal(a.getAnimal() != null ? animalCache.get(a.getAnimal().getId()) : null)
                .application(a.getApplication() != null ? applicationCache.get(a.getApplication().getId()) : null)
                .build()).toList();
        adoptionNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " adoptions to Neo4j");
    }

    private void migrateFosterCare() {
        var fosterCares = fosterCareRepository.findAll();
        var nodes = fosterCares.stream().map(f -> FosterCareNode.builder()
                .id(toStringOrNull(f.getId()))
                .startDate(f.getStartDate())
                .endDate(f.getEndDate())
                .isActive(f.getIsActive())
                .animal(f.getAnimal() != null ? animalCache.get(f.getAnimal().getId()) : null)
                .fosterParent(f.getFosterParent() != null ? userCache.get(f.getFosterParent().getId()) : null)
                .build()).toList();
        fosterCareNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " foster cares to Neo4j");
    }

    private void migrateMedicalRecords() {
        var records = medicalRecordRepository.findAll();
        var nodes = records.stream().map(r -> MedicalRecordNode.builder()
                .id(toStringOrNull(r.getId()))
                .date(r.getDate())
                .diagnosis(r.getDiagnosis())
                .treatment(r.getTreatment())
                .cost(r.getCost())
                .animal(r.getAnimal() != null ? animalCache.get(r.getAnimal().getId()) : null)
                .veterinarian(r.getVeterinarian() != null ? vetCache.get(r.getVeterinarian().getId()) : null)
                .build()).toList();
        medicalRecordNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " medical records to Neo4j");
    }

    private void migrateVaccinations() {
        var vaccinations = vaccinationRepository.findAll();
        var nodes = vaccinations.stream().map(v -> VaccinationNode.builder()
                .id(toStringOrNull(v.getId()))
                .dateAdministered(v.getDateAdministered())
                .nextDueDate(v.getNextDueDate())
                .animal(v.getAnimal() != null ? animalCache.get(v.getAnimal().getId()) : null)
                .veterinarian(v.getVeterinarian() != null ? vetCache.get(v.getVeterinarian().getId()) : null)
                .vaccinationType(v.getVaccinationType() != null ? vaccinationTypeCache.get(v.getVaccinationType().getId()) : null)
                .build()).toList();
        vaccinationNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " vaccinations to Neo4j");
    }

    private void migrateVaccineTypeSpecies() {
        var vtSpecies = vaccineTypeSpeciesRepository.findAll();
        var nodes = vtSpecies.stream().map(v -> VaccineTypeSpeciesNode.builder()
                .id(toStringOrNull(v.getId()))
                .species(v.getSpecies() != null ? speciesCache.get(v.getSpecies().getId()) : null)
                .vaccinationType(v.getVaccinationType() != null ? vaccinationTypeCache.get(v.getVaccinationType().getId()) : null)
                .build()).toList();
        vaccineTypeSpeciesNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " vaccine type species to Neo4j");
    }

    private void migrateRefreshTokens() {
        var tokens = refreshTokenRepository.findAll();
        var nodes = tokens.stream().map(t -> RefreshTokenNode.builder()
                .id(toStringOrNull(t.getId()))
                .token(t.getToken())
                .expiresAt(t.getExpiresAt())
                .revoked(t.getRevoked())
                .user(t.getUser() != null ? userCache.get(t.getUser().getId()) : null)
                .build()).toList();
        refreshTokenNeo4jRepository.saveAll(nodes);
        System.out.println("Migrated " + nodes.size() + " refresh tokens to Neo4j");
    }

    private String toStringOrNull(Long id) {
        return id != null ? id.toString() : null;
    }
}
