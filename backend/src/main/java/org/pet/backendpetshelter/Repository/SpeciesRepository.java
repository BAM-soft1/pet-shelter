package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.Species;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface SpeciesRepository extends JpaRepository<Species, Long> {
    Species findById(long id);
    Optional<Species> findByName(String name);
}
