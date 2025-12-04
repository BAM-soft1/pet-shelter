package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.pet.backendpetshelter.Roles;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNode {

    @Id
    private String id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean isActive;
    private Roles role;
}
