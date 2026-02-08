package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "adoption_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionApplicationDocument {
    @Id
    private String id;

    private Date applicationDate;
    private String status;
    private String description;
    private boolean isActive;

    private EmbeddedApplicant applicant;
    private EmbeddedAnimal animal;
    private EmbeddedReviewer reviewedBy;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedApplicant {
        private String name;
        private String email;
        private String phone;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedAnimal {
        private String name;
        private String species;
        private String breed;
        private String status;
        private String imageUrl;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedReviewer {
        private String name;
        private String email;
    }
}