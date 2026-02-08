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

    // EMBEDDED — ikke userId/animalId
    private EmbeddedUser user;
    private EmbeddedAnimal animal;

    private Date applicationDate;
    private String status;
    private String description;
    private boolean isActive;

    private EmbeddedUser reviewedBy;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedUser {
        private String id;
        private String name;
        private String email;
        private String phone;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedAnimal {
        private String id;
        private String name;
        private String species;
        private String breed;
        private String status;
        private String imageUrl;
    }
}