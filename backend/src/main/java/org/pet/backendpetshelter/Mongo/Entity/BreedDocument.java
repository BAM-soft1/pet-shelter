package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "breeds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreedDocument {
    @Id
    private String id;
    private String name;
    private String description;

    private EmbeddedSpecies species;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedSpecies {
        private String id;
        private String name;
    }
}