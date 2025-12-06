package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Veterinarian")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeterinarianNode {

    @Id
    private String id;

    private String licenseNumber;
    private String clinicName;
    private Boolean isActive;

    @Relationship(type = "IS_USER", direction = Relationship.Direction.OUTGOING)
    private UserNode user;
}
