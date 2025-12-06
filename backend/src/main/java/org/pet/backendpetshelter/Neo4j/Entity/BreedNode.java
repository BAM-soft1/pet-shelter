package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Breed")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreedNode {

    @Id
    private String id;

    private String name;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private SpeciesNode species;
}
