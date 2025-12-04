package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Species")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeciesNode {

    @Id
    private String id;

    private String name;
}
