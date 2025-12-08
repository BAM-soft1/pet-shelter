package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.pet.backendpetshelter.Status;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;

@Node("AdoptionApplication")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionApplicationNode {

    @Id
    private String id;

    private String description;
    private Date applicationDate;
    private Status status;
    private Boolean isActive;

    @Relationship(type = "APPLIED_BY", direction = Relationship.Direction.OUTGOING)
    private UserNode user;

    @Relationship(type = "FOR_ANIMAL", direction = Relationship.Direction.OUTGOING)
    private AnimalNode animal;

    @Relationship(type = "REVIEWED_BY", direction = Relationship.Direction.OUTGOING)
    private UserNode reviewedByUser;
}
