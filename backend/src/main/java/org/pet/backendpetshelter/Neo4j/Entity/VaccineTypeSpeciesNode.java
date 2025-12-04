package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("VaccineTypeSpecies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccineTypeSpeciesNode {

    @Id
    private String id;

    @Relationship(type = "FOR_SPECIES", direction = Relationship.Direction.OUTGOING)
    private SpeciesNode species;

    @Relationship(type = "VACCINE_TYPE", direction = Relationship.Direction.OUTGOING)
    private VaccinationTypeNode vaccinationType;
}
