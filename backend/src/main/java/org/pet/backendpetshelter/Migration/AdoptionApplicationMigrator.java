package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Mongo.Entity.AdoptionApplicationDocument;
import org.pet.backendpetshelter.Mongo.Repository.AdoptionApplicationMongoRepository;
import org.pet.backendpetshelter.Repository.AdoptionApplicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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
            System.out.println("AdoptionApplication migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting adoption application migration...");

        var applications = adoptionApplicationRepository.findAll();
        var docs = applications.stream().map(this::toDocument).collect(Collectors.toList());
        adoptionApplicationMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " adoption applications to MongoDB");
    }

    private AdoptionApplicationDocument toDocument(AdoptionApplication a) {
        return AdoptionApplicationDocument.builder()
                .id(a.getId().toString())
                .applicationDate(a.getApplicationDate())
                .status(a.getStatus() != null ? a.getStatus().name() : null)
                .description(a.getDescription())
                .isActive(a.getIsActive() != null ? a.getIsActive() : true)
                .applicant(a.getUser() != null ?
                        AdoptionApplicationDocument.EmbeddedApplicant.builder()
                                .name(a.getUser().getFirstName() + " " + a.getUser().getLastName())
                                .email(a.getUser().getEmail())
                                .phone(a.getUser().getPhone())
                                .build()
                        : null)
                .animal(a.getAnimal() != null ?
                        AdoptionApplicationDocument.EmbeddedAnimal.builder()
                                .name(a.getAnimal().getName())
                                .species(a.getAnimal().getSpecies() != null ? a.getAnimal().getSpecies().getName() : null)
                                .breed(a.getAnimal().getBreed() != null ? a.getAnimal().getBreed().getName() : null)
                                .status(a.getAnimal().getStatus() != null ? a.getAnimal().getStatus().name() : null)
                                .imageUrl(a.getAnimal().getImageUrl())
                                .build()
                        : null)
                .reviewedBy(a.getReviewedByUser() != null ?
                        AdoptionApplicationDocument.EmbeddedReviewer.builder()
                                .name(a.getReviewedByUser().getFirstName() + " " + a.getReviewedByUser().getLastName())
                                .email(a.getReviewedByUser().getEmail())
                                .build()
                        : null)
                .build();
    }
}