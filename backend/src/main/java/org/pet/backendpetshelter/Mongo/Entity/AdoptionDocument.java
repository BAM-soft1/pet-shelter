package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "adoptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionDocument {
    @Id
    private String id;

    private Date adoptionDate;
    private boolean isActive;

    private EmbeddedAdopter adopter;
    private EmbeddedAnimal animal;
    private EmbeddedApplication application;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedAdopter {
        private String name;
        private String email;
        private String phone;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedAnimal {
        private String name;
        private String species;
        private String breed;
        private String imageUrl;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedApplication {
        private Date applicationDate;
        private String status;
        private String description;
    }
}