package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;

@Node("Adoption")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionNode {

    @Id
    private String id;

    private Date adoptionDate;
    private Boolean isActive;

    @Relationship(type = "FROM_APPLICATION", direction = Relationship.Direction.OUTGOING)
    private AdoptionApplicationNode application;
}
