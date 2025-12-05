package org.pet.backendpetshelter.Neo4j.Repository;

import org.pet.backendpetshelter.Neo4j.Entity.VeterinarianNode;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"neo4j", "migrate-neo4j"})
public interface VeterinarianNeo4jRepository extends Neo4jRepository<VeterinarianNode, String> {
}
