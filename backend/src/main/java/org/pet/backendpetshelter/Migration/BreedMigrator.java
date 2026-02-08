package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.Breed;
import org.pet.backendpetshelter.Mongo.Entity.BreedDocument;
import org.pet.backendpetshelter.Mongo.Repository.BreedMongoRepository;
import org.pet.backendpetshelter.Repository.BreedRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("migrate-mongo")
public class BreedMigrator implements CommandLineRunner {

    private final BreedRepository breedRepository;
    private final BreedMongoRepository breedMongoRepository;

    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;

    public BreedMigrator(BreedRepository breedRepository, BreedMongoRepository breedMongoRepository) {
        this.breedRepository = breedRepository;
        this.breedMongoRepository = breedMongoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("Breed migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting breed migration...");

        var breeds = breedRepository.findAll();
        var docs = breeds.stream().map(this::toDocument).toList();
        breedMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " breeds to MongoDB");
    }

    private BreedDocument toDocument(Breed b) {
        return BreedDocument.builder()
                .id(toStringOrNull(b.getId()))
                .name(b.getName())
                .description(null)
                .species(b.getSpecies() != null ?
                        BreedDocument.EmbeddedSpecies.builder()
                                .id(toStringOrNull(b.getSpecies().getId()))
                                .name(b.getSpecies().getName())
                                .build()
                        : null)
                .build();
    }

    private String toStringOrNull(Long id) {
        return id != null ? id.toString() : null;
    }
}