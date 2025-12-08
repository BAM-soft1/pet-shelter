package org.pet.backendpetshelter.Neo4j.Entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;

@Node("RefreshToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenNode {

    @Id
    private String id;

    private String token;
    private Instant expiresAt;
    private Boolean revoked;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private UserNode user;
}
