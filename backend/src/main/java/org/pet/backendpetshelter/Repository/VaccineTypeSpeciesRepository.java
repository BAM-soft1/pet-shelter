package org.pet.backendpetshelter.Repository;

import org.pet.backendpetshelter.Entity.VaccineTypeSpecies;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface VaccineTypeSpeciesRepository extends JpaRepository<VaccineTypeSpecies, Long> {
    VaccineTypeSpecies findById(long id);
}
