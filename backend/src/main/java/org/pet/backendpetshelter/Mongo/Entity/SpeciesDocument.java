package org.pet.backendpetshelter.Mongo.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "species")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeciesDocument {
    @Id
    private String id;
    private String name;
    private String description;
}