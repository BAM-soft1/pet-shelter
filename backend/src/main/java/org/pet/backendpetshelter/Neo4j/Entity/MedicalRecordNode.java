package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;

@Node("MedicalRecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordNode {

    @Id
    private String id;

    private Date date;
    private String diagnosis;
    private String treatment;
    private int cost;

    @Relationship(type = "FOR_ANIMAL", direction = Relationship.Direction.OUTGOING)
    private AnimalNode animal;

    @Relationship(type = "BY_VET", direction = Relationship.Direction.OUTGOING)
    private VeterinarianNode veterinarian;
}
