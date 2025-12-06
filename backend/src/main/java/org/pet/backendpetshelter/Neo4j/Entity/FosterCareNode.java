package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;

@Node("FosterCare")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FosterCareNode {

    @Id
    private String id;

    private Date startDate;
    private Date endDate;
    private Boolean isActive;

    @Relationship(type = "CARING_FOR", direction = Relationship.Direction.OUTGOING)
    private AnimalNode animal;

    @Relationship(type = "FOSTERED_BY", direction = Relationship.Direction.OUTGOING)
    private UserNode fosterParent;
}
