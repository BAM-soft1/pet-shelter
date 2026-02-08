package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "medical_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDocument {
    @Id
    private String id;

    private EmbeddedAnimal animal;
    private EmbeddedVet veterinarian;
    private Date date;
    private String diagnosis;
    private String treatment;
    private double cost;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedAnimal {
        private String id;
        private String name;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EmbeddedVet {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
    }
}