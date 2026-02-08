package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vaccine_type_species")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccineTypeSpeciesDocument {
    @Id
    private String id;

    private EmbeddedSpecies species;
    private EmbeddedVaccinationType vaccinationType;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedSpecies {
        private String id;
        private String name;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedVaccinationType {
        private String id;
        private String vaccineName;
        private String description;
        private int durationMonths;
        private boolean requiredForAdoption;
    }
}