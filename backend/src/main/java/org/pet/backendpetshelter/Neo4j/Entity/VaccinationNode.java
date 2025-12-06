package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;

@Node("Vaccination")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationNode {

    @Id
    private String id;

    private Date dateAdministered;
    private Date nextDueDate;

    @Relationship(type = "FOR_ANIMAL", direction = Relationship.Direction.OUTGOING)
    private AnimalNode animal;

    @Relationship(type = "BY_VET", direction = Relationship.Direction.OUTGOING)
    private VeterinarianNode veterinarian;

    @Relationship(type = "OF_TYPE", direction = Relationship.Direction.OUTGOING)
    private VaccinationTypeNode vaccinationType;
}
