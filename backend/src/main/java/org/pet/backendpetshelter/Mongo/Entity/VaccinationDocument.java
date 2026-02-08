package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "vaccinations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDocument {
    @Id
    private String id;

    private EmbeddedAnimal animal;
    private EmbeddedVet veterinarian;
    private EmbeddedVaccinationType vaccinationType;
    private Date dateAdministered;
    private Date nextDueDate;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedAnimal {
        private String id;
        private String name;
        private String status;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedVet {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
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