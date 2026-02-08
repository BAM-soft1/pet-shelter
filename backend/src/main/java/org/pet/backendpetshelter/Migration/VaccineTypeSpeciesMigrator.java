package org.pet.backendpetshelter.Migration;

import org.pet.backendpetshelter.Entity.VaccineTypeSpecies;
import org.pet.backendpetshelter.Mongo.Entity.VaccineTypeSpeciesDocument;
import org.pet.backendpetshelter.Mongo.Repository.VaccineTypeSpeciesMongoRepository;
import org.pet.backendpetshelter.Repository.VaccineTypeSpeciesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("migrate-mongo")
public class VaccineTypeSpeciesMigrator implements CommandLineRunner {

    private final VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository;
    private final VaccineTypeSpeciesMongoRepository vaccineTypeSpeciesMongoRepository;

    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;

    public VaccineTypeSpeciesMigrator(VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository,
                                      VaccineTypeSpeciesMongoRepository vaccineTypeSpeciesMongoRepository) {
        this.vaccineTypeSpeciesRepository = vaccineTypeSpeciesRepository;
        this.vaccineTypeSpeciesMongoRepository = vaccineTypeSpeciesMongoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        if (!migrationEnabled) {
            System.out.println("VaccineTypeSpecies migration is disabled. Skipping...");
            return;
        }

        System.out.println("Starting vaccine type species migration...");

        var entries = vaccineTypeSpeciesRepository.findAll();
        var docs = entries.stream().map(this::toDocument).toList();
        vaccineTypeSpeciesMongoRepository.saveAll(docs);

        System.out.println("Migrated " + docs.size() + " vaccine type species to MongoDB");
    }

    private VaccineTypeSpeciesDocument toDocument(VaccineTypeSpecies vts) {
        return VaccineTypeSpeciesDocument.builder()
                .id(toStringOrNull(vts.getId()))
                .species(vts.getSpecies() != null ?
                        VaccineTypeSpeciesDocument.EmbeddedSpecies.builder()
                                .id(toStringOrNull(vts.getSpecies().getId()))
                                .name(vts.getSpecies().getName())
                                .build()
                        : null)
                .vaccinationType(vts.getVaccinationType() != null ?
                        VaccineTypeSpeciesDocument.EmbeddedVaccinationType.builder()
                                .id(toStringOrNull(vts.getVaccinationType().getId()))
                                .vaccineName(vts.getVaccinationType().getVaccineName())
                                .description(vts.getVaccinationType().getDescription())
                                .durationMonths(vts.getVaccinationType().getDurationMonths())
                                .requiredForAdoption(vts.getVaccinationType().getRequiredForAdoption())
                                .build()
                        : null)
                .build();
    }

    private String toStringOrNull(Long id) {
        return id != null ? id.toString() : null;
    }
}