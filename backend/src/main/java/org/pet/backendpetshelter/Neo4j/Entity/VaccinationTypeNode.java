package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("VaccinationType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationTypeNode {

    @Id
    private String id;

    private String vaccineName;
    private String description;
    private int durationMonths;
    private int requiredForAdoption;
}
