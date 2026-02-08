package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Mongo.Entity.AdoptionApplicationDocument;
import org.springframework.transaction.annotation.Transactional;
import org.pet.backendpetshelter.Mongo.Repository.AdoptionApplicationMongoRepository;
import org.pet.backendpetshelter.Repository.AdoptionApplicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("migrate-mongo")
public class AdoptionApplicationMigrator implements CommandLineRunner {

    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final AdoptionApplicationMongoRepository adoptionApplicationMongoRepository;

    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;

    public AdoptionApplicationMigrator(AdoptionApplicationRepository adoptionApplicationRepository,
                                       AdoptionApplicationMongoRepository adoptionApplicationMongoRepository) {
        this.adoptionApplicationRepository = adoptionApplicationRepository;
        this.adoptionApplicationMongoRepository = adoptionApplicationMongoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("Adoptionapplication migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting adoptionapplication migration...");

        var adoptionsApplications = adoptionApplicationRepository.findAll();

        var docs = adoptionsApplications.stream()
                .map(this::toDocument)
                .toList();

        adoptionApplicationMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " adoption applications to MongoDB");
    }

    private AdoptionApplicationDocument toDocument(AdoptionApplication a) {
        return AdoptionApplicationDocument.builder()
                .id(toStringOrNull(a.getId()))
                .user(toEmbeddedUser(a.getUser()))
                .animal(toEmbeddedAnimal(a.getAnimal()))
                .applicationDate(a.getApplicationDate())
                .status(a.getStatus() != null ? a.getStatus().name() : null)
                .description(a.getDescription())
                .isActive(a.getIsActive())
                .reviewedBy(a.getReviewedByUser() != null ? toEmbeddedUser(a.getReviewedByUser()) : null)
                .build();
    }

    private AdoptionApplicationDocument.EmbeddedUser toEmbeddedUser(org.pet.backendpetshelter.Entity.User u) {
        if (u == null) return null;
        return AdoptionApplicationDocument.EmbeddedUser.builder()
                .id(toStringOrNull(u.getId()))
                .name(u.getFirstName() + " " + u.getLastName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .build();
    }

    private AdoptionApplicationDocument.EmbeddedAnimal toEmbeddedAnimal(org.pet.backendpetshelter.Entity.Animal a) {
        if (a == null) return null;
        return AdoptionApplicationDocument.EmbeddedAnimal.builder()
                .id(toStringOrNull(a.getId()))
                .name(a.getName())
                .species(a.getSpecies() != null ? a.getSpecies().getName() : null)
                .breed(a.getBreed() != null ? a.getBreed().getName() : null)
                .status(a.getStatus() != null ? a.getStatus().name() : null)
                .imageUrl(a.getImageUrl())
                .build();
    }

    private String toStringOrNull(Long id) {
        return id != null ? id.toString() : null;
    }
}