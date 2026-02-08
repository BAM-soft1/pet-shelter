package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "animals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDocument {
    @Id
    private String id;

    private String name;
    private String sex;
    private Date birthDate;
    private Date intakeDate;
    private String status;
    private double price;
    private boolean isActive;
    private String imageUrl;

    private EmbeddedSpecies species;
    private EmbeddedBreed breed;
    private List<EmbeddedVaccination> vaccinations;
    private List<EmbeddedMedicalRecord> medicalRecords;
    private List<EmbeddedAdoptionApplication> adoptionApplications;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedSpecies {
        private String name;
        private String description;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedBreed {
        private String name;
        private String speciesName;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedVaccination {
        private String vaccineName;
        private String description;
        private int durationMonths;
        private boolean requiredForAdoption;
        private Date dateAdministered;
        private Date nextDueDate;
        private EmbeddedVet administeredBy;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedMedicalRecord {
        private Date date;
        private String diagnosis;
        private String treatment;
        private double cost;
        private EmbeddedVet veterinarian;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedVet {
        private String firstName;
        private String lastName;
        private String email;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedAdoptionApplication {
        private Date applicationDate;
        private String status;
        private String description;
        private EmbeddedApplicant applicant;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedApplicant {
        private String name;
        private String email;
        private String phone;
    }
}