package org.pet.backendpetshelter.Neo4j.Repository;

import org.pet.backendpetshelter.Neo4j.Entity.AdoptionNode;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"neo4j", "migrate-neo4j"})
public interface AdoptionNeo4jRepository extends Neo4jRepository<AdoptionNode, String> {
}
