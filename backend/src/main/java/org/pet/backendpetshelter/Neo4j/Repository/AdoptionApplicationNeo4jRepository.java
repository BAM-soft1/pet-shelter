package org.pet.backendpetshelter.Neo4j.Repository;

import org.pet.backendpetshelter.Neo4j.Entity.AdoptionApplicationNode;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"neo4j", "migrate-neo4j"})
public interface AdoptionApplicationNeo4jRepository extends Neo4jRepository<AdoptionApplicationNode, String> {
}
