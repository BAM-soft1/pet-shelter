package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;

@Node("Animal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalNode {

    @Id
    private String id;

    private String name;
    private Date birthDate;
    private String sex;
    private Date intakeDate;
    private String status;
    private int price;
    private Boolean isActive;
    private String imageUrl;

    @Relationship(type = "IS_SPECIES", direction = Relationship.Direction.OUTGOING)
    private SpeciesNode species;

    @Relationship(type = "IS_BREED", direction = Relationship.Direction.OUTGOING)
    private BreedNode breed;
}
