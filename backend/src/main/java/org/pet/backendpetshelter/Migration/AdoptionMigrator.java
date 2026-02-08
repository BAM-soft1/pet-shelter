package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.Adoption;
import org.pet.backendpetshelter.Mongo.Entity.AdoptionDocument;
import org.pet.backendpetshelter.Mongo.Repository.AdoptionMongoRepository;
import org.pet.backendpetshelter.Repository.AdoptionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
@Profile("migrate-mongo")
public class AdoptionMigrator implements CommandLineRunner {

    private final AdoptionRepository adoptionRepository;
    private final AdoptionMongoRepository adoptionMongoRepository;

    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;

    public AdoptionMigrator(AdoptionRepository adoptionRepository,
                             AdoptionMongoRepository adoptionMongoRepository) {
        this.adoptionRepository = adoptionRepository;
        this.adoptionMongoRepository = adoptionMongoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("Adoption migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting adoption migration...");

        var adoptions = adoptionRepository.findAll();
        var docs = adoptions.stream().map(this::toDocument).collect(Collectors.toList());
        adoptionMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " adoptions to MongoDB");
    }

    private AdoptionDocument toDocument(Adoption a) {
        var app = a.getApplication();
        var user = app != null ? app.getUser() : null;
        var animal = app != null ? app.getAnimal() : null;

        return AdoptionDocument.builder()
                .id(a.getId().toString())
                .adoptionDate(a.getAdoptionDate())
                .isActive(a.getIsActive() != null ? a.getIsActive() : true)
                .adopter(user != null ?
                        AdoptionDocument.EmbeddedAdopter.builder()
                                .name(user.getFirstName() + " " + user.getLastName())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .build()
                        : null)
                .animal(animal != null ?
                        AdoptionDocument.EmbeddedAnimal.builder()
                                .name(animal.getName())
                                .species(animal.getSpecies() != null ? animal.getSpecies().getName() : null)
                                .breed(animal.getBreed() != null ? animal.getBreed().getName() : null)
                                .imageUrl(animal.getImageUrl())
                                .build()
                        : null)
                .application(app != null ?
                        AdoptionDocument.EmbeddedApplication.builder()
                                .applicationDate(app.getApplicationDate())
                                .status(app.getStatus() != null ? app.getStatus().name() : null)
                                .description(app.getDescription())
                                .build()
                        : null)
                .build();
    }
}